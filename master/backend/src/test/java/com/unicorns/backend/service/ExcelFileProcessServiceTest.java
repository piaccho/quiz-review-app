package com.unicorns.backend.service;

import com.unicorns.backend.config.BackendConfig;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.repository.QuizRepository;
import com.unicorns.backend.utill.PrizeParser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class ExcelFileProcessServiceTest {


    @Mock
    private BackendConfig config;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private PrizeParser prizeParser;

    @InjectMocks
    private ExcelFileProcessService service;

    // TODO: FIX TEST
    @Test
    public void getQuizFromFileTest() throws IOException {
        // given
        Path path = Paths.get("src/test/resources/quiz.xlsx");
        String name = "file";
        String originalFileName = "file.xlsx";
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);

        // when
        Quiz result = service.getQuizFromFile(file);

        // then
        assertNotNull(result);
        System.out.println(result);
    }
}