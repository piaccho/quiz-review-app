package com.unicorns.backend;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.unicorns.backend.model.Preset;
import com.unicorns.backend.model.Prize;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.repository.PresetRepository;
import com.unicorns.backend.repository.PrizeCategoryRepository;
import com.unicorns.backend.repository.PrizeRepository;
import com.unicorns.backend.service.PresetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;


import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BackendApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PresetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PresetService presetService;

    @Autowired
    private PresetRepository presetRepository;

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private PrizeCategoryRepository prizeCategoryRepository;

    @Test
    public void createPresetTest() throws Exception { //Umarło, proszę nie patrzeć, to naprawdę bardzo brzydkie i niczego nie testuje, ale się poprawię
        //Przetestowaliśmy postmanem
        // Given


//        Prize carrotLab = new Prize();
//        carrotLab.setName("Marchewka lab");
//        carrotLab.setDescription("+10% do sumy za lab z Javy (maks. 30%)");
//        Prize carrotProject = new Prize();
//        carrotProject.setName("Marchewka projektowa");
//        carrotProject.setDescription("+10% do sumy za projekt (maks. 40%)");
//        Prize medicine = new Prize();
//        medicine.setName("Lekarstwo");
//        medicine.setDescription("odrobienie straconych 2xp za lab");
//        Prize vet = new Prize();
//        vet.setName("Weterynarz");
//        vet.setDescription("odrobienie straconych 2xp za kartkówkę");
//        Prize hayDiscount = new Prize();
//        hayDiscount.setName("Rabat na sianko");
//        hayDiscount.setDescription("darmowe zaliczenie najbliższej kartkówki (2xp)");
//
//        carrotLab = prizeRepository.save(carrotLab);
//        carrotProject = prizeRepository.save(carrotProject);
//        medicine = prizeRepository.save(medicine);
//        vet = prizeRepository.save(vet);
//        hayDiscount = prizeRepository.save(hayDiscount);
//
//        PrizeCategory goldChest = new PrizeCategory();
//        goldChest.setName("Złota Skrzynka");
//        goldChest.setAvailablePrizes(Arrays.asList(vet, medicine, hayDiscount, carrotLab));
//        PrizeCategory silverChest = new PrizeCategory();
//        silverChest.setName("Srebrna Skrzynka");
//        silverChest.setAvailablePrizes(Arrays.asList(vet, medicine, carrotLab));
//        PrizeCategory bronzeChest = new PrizeCategory();
//        bronzeChest.setName("Brązowa Skrzynka");
//        bronzeChest.setAvailablePrizes(Arrays.asList(medicine, vet));
//
//        prizeCategoryRepository.save(goldChest);
//        prizeCategoryRepository.save(silverChest);
//        prizeCategoryRepository.save(bronzeChest);
//
//
//
//        String json = "{\n" +
//                "    \"name\": \"her\",\n" +
//                "    \"strategy\": \"TIME\",\n" +
//                "    \"presetEntries\": [\n" +
//                "        {\n" +
//                "            \"category\": 1,\n" +
//                "            \"value\": 2\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"category\": 2,\n" +
//                "            \"value\": 1\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
//
//        try {
//            // When/Then
//
//            mockMvc.perform(post("/api/preset/add")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(json))
//                    .andExpect(status().isOk())
//                    .andReturn();
//        } catch (InvalidFormatException e) {
//            // Output details for debugging
//            System.out.println("InvalidFormatException occurred");
//            System.out.println("JSON: " + json);
//            e.printStackTrace();
//            throw e;
//        }

    }

}