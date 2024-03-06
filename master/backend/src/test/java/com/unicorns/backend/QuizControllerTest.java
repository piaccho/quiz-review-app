package com.unicorns.backend;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.repository.QuizRepository;
import com.unicorns.backend.service.QuizService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BackendApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    public void createdQuizWithValidFile_ReturnsStatus200() throws Exception {
        // Given
        String originalName = "src/test/resources/quiz.xlsx";
        File testFile = new File(originalName);
        InputStream stream = new FileInputStream(testFile);
        MockMultipartFile file = new MockMultipartFile("file", testFile.getName(), MediaType.ALL_VALUE, stream);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/quizzes")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void createdQuizWithInvalidFile_ReturnsStatus400() throws Exception {
        // Given
        String originalName = "src/test/resources/quiz_bad.xlsx";
        File testFile = new File(originalName);
        InputStream stream = new FileInputStream(testFile);
        MockMultipartFile file = new MockMultipartFile("file", testFile.getName(), MediaType.ALL_VALUE, stream);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/quiz/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void getExistingQuizById_ReturnsStatus200() throws Exception {
        // Given
        long existingQuizId = 1L;
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(existingQuizId);
        Mockito.when(quizService.getQuizById(existingQuizId)).thenReturn(Optional.of(existingQuiz));

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quiz/{id}", existingQuizId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNonExistingQuizById_ReturnsStatus404() throws Exception {
        // Given
        long nonExistingQuizId = 999L;
        Mockito.when(quizService.getQuizById(nonExistingQuizId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quiz/{id}", nonExistingQuizId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getAllQuizzes_ReturnsStatus200() throws Exception {
        // Given
        Mockito.when(quizService.getAllQuizzes()).thenReturn(null);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quiz/all"))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void getAllQuizzesIds_ReturnsCorrectIds() throws Exception {
        // Given
        Quiz quiz1 = new Quiz();
        Quiz quiz2 = new Quiz();
        Quiz quiz3 = new Quiz();
        quizRepository.save(quiz1);
        quizRepository.save(quiz2);
        quizRepository.save(quiz3);

        // When:
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/quiz/all-ids"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        // Then:
        String responseBody = result.getResponse().getContentAsString();
        List<Long> quizIds = objectMapper.readValue(responseBody, List.class);

        assertTrue(quizIds.contains(quiz1.getId()));
        assertTrue(quizIds.contains(quiz2.getId()));
        assertTrue(quizIds.contains(quiz3.getId()));
    }
}
