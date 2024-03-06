package com.unicorns.backend.service;

import com.unicorns.backend.model.Prize;
import com.unicorns.backend.repository.PresetRepository;
import com.unicorns.backend.repository.PrizeRepository;
import com.unicorns.backend.request.PrizeCategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootTest
class PrizeServiceTest {

    private PrizeService underTest;

    @Mock
    private PrizeRepository prizeRepository;



    @BeforeEach
    void setUp() {
        underTest = new PrizeService(prizeRepository);
    }
    @Test
    void getPrizeByIdWhenPrizeExists() {
        //given
        long prizeId = 1;
        Prize expectedPrize = new Prize();
        expectedPrize.setId(prizeId);
        expectedPrize.setName("Marchewka lab");
        expectedPrize.setDescription("darmowa kartkówka");
        given(prizeRepository.findById(prizeId)).willReturn(Optional.of(expectedPrize));

        //when
        Optional<Prize> actualPrize = underTest.getPrizeById(prizeId);
        //then
        assertTrue(actualPrize.isPresent());
        assertEquals(expectedPrize, actualPrize.get());
        verify(prizeRepository).findById(prizeId);

    }
    @Test
    void getPrizeByIdWhenPrizeDoesNotExist() {
        //given
        long prizeId = 1;
        given(prizeRepository.findById(prizeId)).willReturn(Optional.empty());
        //when
        Optional<Prize> actualPrize = underTest.getPrizeById(prizeId);
        //then
        assertFalse(actualPrize.isPresent());
        verify(prizeRepository).findById(prizeId);

    }

    @Test
    void GetAllPrizes() {
        underTest.getAllPrizes();
        verify(prizeRepository).findAll();
    }

    @Test
    void SavePrizeSuccessfully() {
        //given
        Prize prizeToAdd = new Prize();

        //when
        underTest.savePrize(prizeToAdd);

        //then
        ArgumentCaptor<Prize> prizeArgumentCaptor = ArgumentCaptor.forClass(Prize.class);
        verify(prizeRepository).save(prizeArgumentCaptor.capture());

        Prize capturedPrize = prizeArgumentCaptor.getValue();
        assertEquals(capturedPrize,prizeToAdd);

    }

    @Test
    void ReturnPrizeIfExists() {
        //given
        String prizeName = "Rabat na sianko";
        long prizeId = 1;
        Prize expectedPrize = new Prize();
        expectedPrize.setId(prizeId);
        expectedPrize.setName(prizeName);
        expectedPrize.setDescription("darmowa kartkówka");
        given(prizeRepository.findAll()).willReturn(List.of(expectedPrize));
        //when
        Optional <Prize> actualPrize = underTest.checkIfPrizeExists(prizeName);
        //then
        assertTrue(actualPrize.isPresent());
        assertEquals(expectedPrize,actualPrize.get());
        verify(prizeRepository).findAll();
    }

    @Test
    void ReturnEmptyIfPrizeNotExists() {
        //given
        String prizeName = "Rabat na sianko";
        given(prizeRepository.findAll()).willReturn(List.of());
        //when
        Optional <Prize> actualPrize = underTest.checkIfPrizeExists(prizeName);
        //then
        assertFalse(actualPrize.isPresent());
        verify(prizeRepository).findAll();
    }


    @Test
    void AddNewPrizeWhenNotExists() {
        //given
        String prizeName = "Rabat na sianko";
        long prizeId = 1;
        Prize expectedPrize = new Prize();
        expectedPrize.setId(prizeId);
        expectedPrize.setName(prizeName);
        expectedPrize.setDescription("darmowa kartkówka");
        given(prizeRepository.findAll()).willReturn(List.of());
        given(prizeRepository.save(expectedPrize)).willReturn(expectedPrize);


        //when
        Optional<Prize> actualPrize =  underTest.addNewPrize(expectedPrize);

        //then
        assertTrue(actualPrize.isPresent());
        assertEquals(expectedPrize,actualPrize.get());
        verify(prizeRepository).findAll();
        verify(prizeRepository).save(expectedPrize);


    }
    @Test
    void NotAddPrizeWhenAlreadyExists() {
        //given
        String prizeName = "Rabat na sianko";
        long prizeId = 1;
        Prize expectedPrize = new Prize();
        expectedPrize.setId(prizeId);
        expectedPrize.setName(prizeName);
        expectedPrize.setDescription("darmowa kartkówka");
        given(prizeRepository.findAll()).willReturn(List.of(expectedPrize));

        //when
        Optional<Prize> actualPrize =  underTest.addNewPrize(expectedPrize);

        //then
        assertFalse(actualPrize.isPresent());
        verify(prizeRepository).findAll();


    }


}