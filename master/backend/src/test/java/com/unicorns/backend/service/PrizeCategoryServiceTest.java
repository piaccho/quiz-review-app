package com.unicorns.backend.service;

import com.unicorns.backend.model.Prize;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.model.Quiz;
import com.unicorns.backend.repository.PrizeCategoryRepository;
import com.unicorns.backend.request.PrizeCategoryRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PrizeCategoryServiceTest {


    private PrizeCategoryService underTest;
    @Mock
    private PrizeService prizeService;


    @Mock
    private PrizeCategoryRepository prizeCategoryRepository;


    @BeforeEach
    void setUp() {
        underTest = new PrizeCategoryService(prizeCategoryRepository,prizeService);
    }
    @Test
    void getCategoryByIdWhenCategoryExists() {
        //given
        long categoryId = 1;
        PrizeCategory expectedCategory = new PrizeCategory();

        expectedCategory.setId(categoryId);
        given(prizeCategoryRepository.findById(categoryId)).willReturn(Optional.of(expectedCategory));

        //when
        Optional<PrizeCategory> actualCategory =  underTest.getCategoryById(categoryId);

        //then
        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());
        verify(prizeCategoryRepository).findById(categoryId);

    }
    @Test
    void getCategoryByIdWhenCategoryDoesNotExist()
    {
        //given
        long categoryId = 1;
        given(prizeCategoryRepository.findById(categoryId)).willReturn(Optional.empty());

        //when
        Optional<PrizeCategory> actualQuiz = underTest.getCategoryById(categoryId);

        //then

        assertFalse(actualQuiz.isPresent());

        verify(prizeCategoryRepository).findById(categoryId);

    }

    @Test
    void GetAllCategories() {
        underTest.getAllCategories();
        verify(prizeCategoryRepository).findAll();
    }

    @Test
    void GetAvailablePrizes() {
        //given
        long id = 1;
        PrizeCategory expectedCategory = new PrizeCategory();
        Prize prize = new Prize();
        expectedCategory.setAvailablePrizes(List.of(prize));
        given(prizeCategoryRepository.findById(id)).willReturn(Optional.of(expectedCategory));

        //when
        Optional<List<Prize>> actualPrizes = underTest.getAvailablePrizes(id);

        //then
        assertTrue(actualPrizes.isPresent());
        assertEquals(List.of(prize), actualPrizes.get());

    }

    @Test
    void SaveCategorySuccessfully() {
        //given
        PrizeCategory categoryToAdd = new PrizeCategory();

        //when
        underTest.saveCategory(categoryToAdd);

        //then
        ArgumentCaptor<PrizeCategory> categoryArgumentCaptor = ArgumentCaptor.forClass(PrizeCategory.class);
        verify(prizeCategoryRepository).save(categoryArgumentCaptor.capture());

        PrizeCategory capturedCategory = categoryArgumentCaptor.getValue();
        assertEquals(capturedCategory,categoryToAdd);
    }

    @Test
    void ReturnCategoryIfExists() {
        //given
        String expectedCategoryName=  "skrzynka";
        long categoryId = 1;
        PrizeCategory expectedCategory = new PrizeCategory();
        PrizeCategory nextCategory = new PrizeCategory();
        nextCategory.setId(categoryId + 1);
        nextCategory.setName("skrzynka2");
        expectedCategory.setId(categoryId);
        expectedCategory.setName(expectedCategoryName);
        given(prizeCategoryRepository.findAll()).willReturn(List.of(expectedCategory,nextCategory));
        //when
        Optional<PrizeCategory> actualCategory = underTest.checkIfCategoryExists(expectedCategoryName);
        //then
        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());
        verify(prizeCategoryRepository).findAll();

    }
    @Test
    void ReturnEmptyIfCategoryNotExists()
    {
        //given
        String expectedCategoryName=  "skrzynka";
        long categoryId = 1;
        PrizeCategory category = new PrizeCategory();
        category.setId(categoryId);
        category.setName("skrzynka2");
        given(prizeCategoryRepository.findAll()).willReturn(List.of(category));
        //when
        Optional<PrizeCategory> actualCategory = underTest.checkIfCategoryExists(expectedCategoryName);

        //then
        assertFalse(actualCategory.isPresent());

        verify(prizeCategoryRepository).findAll();


    }
    @Test
    void AddNewCategoryWhenNotExists() {
        //given
        PrizeCategory categoryToAdd = new PrizeCategory();
        given(prizeCategoryRepository.findAll()).willReturn(List.of());
        given(prizeCategoryRepository.save(categoryToAdd)).willReturn(categoryToAdd);

        //when
        Optional<PrizeCategory> actualCategory = underTest.addNewCategory(categoryToAdd);

        //then
        assertTrue(actualCategory.isPresent());
        assertEquals(categoryToAdd, actualCategory.get());
        verify(prizeCategoryRepository).findAll();
        verify(prizeCategoryRepository).save(categoryToAdd);




    }

    @Test
    void NotAddCategoryWhenAlreadyExists()
    {
        //given
        PrizeCategory categoryToAdd = new PrizeCategory();
        categoryToAdd.setName("skrzynka");
        given(prizeCategoryRepository.findAll()).willReturn(List.of(categoryToAdd));

        //when
        Optional<PrizeCategory> actualCategory = underTest.addNewCategory(categoryToAdd);

        //then
        assertFalse(actualCategory.isPresent());
        verify(prizeCategoryRepository).findAll();



    }


    @Test
    void convertToPrizeCategoryEntitySuccessfully()
    {
        //given
        String requestName = "skrzynka";
        PrizeCategoryRequest request = new PrizeCategoryRequest();
        request.setName(requestName);
        List<Long> prizes = new ArrayList<>();
        prizes.add(1L);
        prizes.add(2L);
        request.setAvailablePrizes(prizes);
        Prize prize1 = new Prize();
        Prize prize2 = new Prize();
        prize1.setId(1L);
        prize2.setId(2L);
        given(prizeService.getPrizeById(1L)).willReturn(Optional.of(prize1));
        given(prizeService.getPrizeById(2L)).willReturn(Optional.of(prize2));

        //when
        PrizeCategory actualCategory = underTest.convertToPrizeCategoryEntity(request);

        //then
        assertNotNull(actualCategory);
        assertEquals(requestName, actualCategory.getName());

    }
    @Test
    void convertToPrizeCategoryEntityThrowsEntityNotFoundException()
    {
        //given
        String requestName = "skrzynka";
        PrizeCategoryRequest request = new PrizeCategoryRequest();
        request.setName(requestName);
        List<Long> prizes = new ArrayList<>();
        prizes.add(1L);
        prizes.add(2L);
        prizes.add(3L);
        request.setAvailablePrizes(prizes);
        Prize prize1 = new Prize();
        Prize prize2 = new Prize();
        prize1.setId(1L);
        prize2.setId(2L);
        given(prizeService.getPrizeById(1L)).willReturn(Optional.of(prize1));
        given(prizeService.getPrizeById(2L)).willReturn(Optional.of(prize2));
        given(prizeService.getPrizeById(2L)).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> underTest.convertToPrizeCategoryEntity(request))
                .isInstanceOf(EntityNotFoundException.class);
    }
}