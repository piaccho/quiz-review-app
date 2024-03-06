package com.unicorns.backend.service;

import com.unicorns.backend.config.BackendConfig;
import com.unicorns.backend.model.*;
import com.unicorns.backend.repository.QuizQuestionRepository;
import com.unicorns.backend.repository.QuizRepository;
import com.unicorns.backend.repository.QuizStatisticsRepository;
import com.unicorns.backend.utill.PrizeParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ExcelFileProcessService {
    private final BackendConfig config;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizStatisticsRepository quizStatisticsRepository;
    private final PrizeParser prizeParser;
    private XSSFSheet quizSheet = null;
    private final Map<String, Integer> indexes = new HashMap<>();
    private int questionsStartIndex;
    private int questionsEndIndex;
    private final Map<Integer, String> questionsMap = new HashMap<>();

    public ExcelFileProcessService(BackendConfig config, QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository, QuizStatisticsRepository quizStatisticsRepository, PrizeParser prizeParser) {
        this.config = config;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizStatisticsRepository = quizStatisticsRepository;
        this.prizeParser = prizeParser;
    }

    public Quiz getQuizFromFile(MultipartFile file) {
        try (InputStream fileInputStream = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

            Quiz quizToSave = new Quiz();

            quizSheet = workbook.getSheetAt(0);
            if(quizSheet.getLastRowNum() <= 1) {
                throw new RuntimeException("Invalid file");
            }

            getIndexes();
            quizToSave.setQuizEntries(getQuizEntries());
//            quizToSave.setQuizStatistics(getQuizStatistics());
            CompletableFuture<QuizStatistics> quizStatisticsFuture = getQuizStatistics();
            quizStatisticsFuture.thenAccept(quizStatistics -> {
                quizToSave.setQuizStatistics(quizStatistics);
                quizRepository.save(quizToSave);
            });

            return quizRepository.save(quizToSave);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getIndexes() {
        Map<String, String> columnNameMap = new HashMap<>();
        columnNameMap.put(config.getMapColumnNickname(), "nickname");
        columnNameMap.put(config.getMapColumnTotalPoints(), "totalPoints");
        columnNameMap.put(config.getMapColumnTimeStart(), "timeStart");
        columnNameMap.put(config.getMapColumnTimeEnd(), "timeEnd");
        columnNameMap.put(config.getMapColumnPreferences(), "preferences");
//        System.out.println("ColumnNameMap: " + columnNameMap);

        Row headerRow = quizSheet.getRow(0);
        for(int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
//            System.out.println("Cell " + i + " : " + cell.getStringCellValue());
            if (columnNameMap.containsKey(cell.getStringCellValue())) {
                indexes.put(columnNameMap.get(cell.getStringCellValue()), i);
            }
            if (cell.getStringCellValue().equals(config.getQuestionsBeforeColumnMapping())) {
                questionsStartIndex = i + 1;
            }
            if (cell.getStringCellValue().equals(config.getQuestionsAfterColumnMapping())) {
                questionsEndIndex = i;
            }
        }
    }

    @Async
    public CompletableFuture<QuizStatistics> getQuizStatistics() {
        QuizStatistics quizStatistics = new QuizStatistics();
        getQuestionsHeaders();
        quizStatistics.setQuizQuestions(getQuizQuestions());
        quizStatisticsRepository.save(quizStatistics);
        return CompletableFuture.completedFuture(quizStatistics);
    }

    private void getQuestionsHeaders() {
        Row headerRow = quizSheet.getRow(0);
        for(int i = questionsStartIndex; i < questionsEndIndex; i += config.getQuestionColumnsRange()) {
            questionsMap.put(i, headerRow.getCell(i).getStringCellValue());
        }
    }

    private List<QuizQuestion> getQuizQuestions() {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for(Map.Entry<Integer, String> entry: questionsMap.entrySet()) {
            int columnIndex = entry.getKey();
            String questionContent = entry.getValue();

            int totalCorrectAnswers = 0;
            int totalWrongAnswers = 0;
            Map<String, Integer> correctOptionsCountMap = new HashMap<>();
            Map<String, Integer> allOptionsCountMap = new HashMap<>();

            for (int rowIndex = 1; rowIndex <= quizSheet.getLastRowNum(); rowIndex++) {
                Row row = quizSheet.getRow(rowIndex);
                if ((int) row.getCell(columnIndex + 1).getNumericCellValue() == 1) {
                    totalCorrectAnswers++;
                    for(String option: row.getCell(columnIndex).getStringCellValue().split(";")) {
                        correctOptionsCountMap.put(option, correctOptionsCountMap.getOrDefault(option, 0) + 1);
                    }
                } else {
                    totalWrongAnswers++;
                    for(String option: row.getCell(columnIndex).getStringCellValue().split(";")) {
                        allOptionsCountMap.put(option, allOptionsCountMap.getOrDefault(option, 0) + 1);
                        if(correctOptionsCountMap.containsKey(option)) {
                            correctOptionsCountMap.put(option, correctOptionsCountMap.get(option) + 1);
                        }
                    }
                }
            }
            for(String key : correctOptionsCountMap.keySet()) {
                allOptionsCountMap.remove(key);
            }
            Map<String, Integer> wrongOptionsCountMap = allOptionsCountMap;

            // dla pytań typu ustawianie bloków
            if (wrongOptionsCountMap.isEmpty()) {
                correctOptionsCountMap = new HashMap<>();
                for (int rowIndex = 1; rowIndex <= quizSheet.getLastRowNum(); rowIndex++) {
                    Row row = quizSheet.getRow(rowIndex);
                    if ((int) row.getCell(columnIndex + 1).getNumericCellValue() == 1) {
                        correctOptionsCountMap.put(String.join(", ", row.getCell(columnIndex).getStringCellValue().split(";")), totalCorrectAnswers);
                        break;
                    }
                }
            }

            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuestionContent(questionContent);
            quizQuestion.setQuestionCorrectAnswers(totalCorrectAnswers);
            quizQuestion.setQuestionWrongAnswers(totalWrongAnswers);
            quizQuestion.setCorrectOptionsCount(correctOptionsCountMap);
            quizQuestion.setWrongOptionsCount(wrongOptionsCountMap);
            quizQuestions.add(quizQuestion);
            quizQuestionRepository.save(quizQuestion);
            System.out.println("Question: " + questionContent + "\nCorrect Options: " + totalCorrectAnswers + "\nWrong Options: " + totalWrongAnswers + "\nCorrect Answers: " + correctOptionsCountMap + "\nWrong Answers: " + wrongOptionsCountMap + "\n");
        }

        return quizQuestions;
    }

    private List<QuizEntry> getQuizEntries() {
        List<QuizEntry> quizEntries = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= quizSheet.getLastRowNum(); rowIndex++) {
            Row row = quizSheet.getRow(rowIndex);
            QuizEntry quizEntryToSave = createQuizDetailFromRow(row);
            quizEntries.add(quizEntryToSave);
        }
        return quizEntries;
    }

    private QuizEntry createQuizDetailFromRow(Row row) {
        QuizEntry quizEntry = new QuizEntry();
        quizEntry.setNickname(getStringCellValue(row, indexes.get("nickname")));
        quizEntry.setTotalPoints(getIntCellValue(row, indexes.get("totalPoints")));
        quizEntry.setTimeDifferenceInSeconds(getTimeDifferenceInSeconds(row));
        quizEntry.setCompletingTime(getDateCellValue(row, indexes.get("timeStart")));
        List<Prize> prizes = prizeParser.parsePrizesFromString(getStringCellValue(row, indexes.get("preferences")));
        quizEntry.setPreferences(prizes);

        return quizEntry;
    }

    private String getStringCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return cell.getStringCellValue();
    }

    private int getIntCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return (int) cell.getNumericCellValue();
    }

    private Date getDateCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        return cell.getDateCellValue();
    }

    private long getTimeDifferenceInSeconds(Row row) {
        Date timeStart = getDateCellValue(row, indexes.get("timeStart"));
        Date timeEnd = getDateCellValue(row, indexes.get("timeEnd"));
        return (timeEnd.getTime() - timeStart.getTime()) / 1000;
    }
}
