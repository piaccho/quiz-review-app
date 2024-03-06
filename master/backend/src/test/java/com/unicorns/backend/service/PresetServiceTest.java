package com.unicorns.backend.service;

import com.unicorns.backend.model.Preset;
import com.unicorns.backend.model.Prize;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.repository.PresetRepository;
import com.unicorns.backend.request.PresetEntryRequest;
import com.unicorns.backend.request.PresetRequest;
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
class PresetServiceTest {


    private PresetService underTest;
    @Mock
    private PresetRepository presetRepository;

    @Mock
    private PrizeCategoryService prizeCategoryService;

    @BeforeEach
    void setUp() {
        underTest = new PresetService(presetRepository,prizeCategoryService);
    }

    @Test
    void getPresetByIdWhenPresetExists() {
        //given
        long presetId = 1;
        Preset expectedPreset = new Preset();
        expectedPreset.setId(presetId);
        given(presetRepository.findById(presetId)).willReturn(Optional.of(expectedPreset));

        //when
        Optional<Preset> actualPreset =  underTest.getPresetById(presetId);

        //then
        assertTrue(actualPreset.isPresent());
        assertEquals(expectedPreset, actualPreset.get());
        verify(presetRepository).findById(presetId);

    }
    @Test
    void getPresetByIdWhenPresetNotExist()
    {
        //given
        long presetId = 1;
        given(presetRepository.findById(presetId)).willReturn(Optional.empty());

        //when
        Optional<Preset> actualPreset = underTest.getPresetById(presetId);

        //then
        assertFalse(actualPreset.isPresent());
        verify(presetRepository).findById(presetId);


    }

    @Test
    void getAllPresets() {
        underTest.getAllPresets();
        verify(presetRepository).findAll();
    }

    @Test
    void savePresetSuccessfully() {
        //given
        Preset presetToAdd = new Preset();

        //when
        underTest.savePreset(presetToAdd);

        //then
        ArgumentCaptor<Preset> presetArgumentCaptor = ArgumentCaptor.forClass(Preset.class);
        verify(presetRepository).save(presetArgumentCaptor.capture());

        Preset capturedPreset = presetArgumentCaptor.getValue();
        assertEquals(capturedPreset,presetToAdd);
    }
    @Test
    void ReturnPresetIfExists() {
        //given

        String presetName = "preset1";
        long presetId = 1;
        Preset expectedPreset = new Preset();
        expectedPreset.setId(presetId);
        expectedPreset.setName(presetName);
        given(presetRepository.findAll()).willReturn(List.of(expectedPreset));
        //when
        Optional <Preset> actualPreset = underTest.checkIfPresetExists(presetName);
        //then

        assertTrue(actualPreset.isPresent());
        assertEquals(expectedPreset,actualPreset.get());
        verify(presetRepository).findAll();
    }

    @Test
    void ReturnEmptyIfPresetNotExists() {
        //given
        String presetName = "preset1";
        given(presetRepository.findAll()).willReturn(List.of());
        //when
        Optional <Preset> actualPrize = underTest.checkIfPresetExists(presetName);
        //then
        assertFalse(actualPrize.isPresent());
        verify(presetRepository).findAll();
    }

    @Test
    void AddNewPresetWhenNotExists() {
        //given
        String presetName = "preset1";
        long presetId = 1;
        Preset expectedPreset = new Preset();
        expectedPreset.setId(presetId);
        expectedPreset.setName(presetName);
        given(presetRepository.findAll()).willReturn(List.of());
        given(presetRepository.save(expectedPreset)).willReturn(expectedPreset);


        //when
        Optional<Preset> actualPreset =  underTest.addNewPreset(expectedPreset);

        //then
        assertTrue(actualPreset.isPresent());
        assertEquals(expectedPreset,actualPreset.get());
        verify(presetRepository).findAll();
        verify(presetRepository).save(expectedPreset);


    }
    @Test
    void NotAddPresetWhenAlreadyExists() {
        //given
        String presetName = "preset1";
        long presetId = 1;
        Preset expectedPreset = new Preset();
        expectedPreset.setId(presetId);
        expectedPreset.setName(presetName);

        given(presetRepository.findAll()).willReturn(List.of(expectedPreset));

        //when
        Optional<Preset> actualPreset =  underTest.addNewPreset(expectedPreset);

        //then
        assertFalse(actualPreset.isPresent());
        verify(presetRepository).findAll();


    }

    @Test
    void convertToPresetEntitySuccessfully() {
        //given
        PrizeCategory category1 = new PrizeCategory();
        PrizeCategory category2 = new PrizeCategory();
        category1.setId(1L);
        category2.setId(2L);
        String presetName = "preset1";
        PresetRequest presetRequest = new PresetRequest();
        presetRequest.setName(presetName);
        PresetEntryRequest presetEntryRequest1 = new PresetEntryRequest();
        presetEntryRequest1.setCategory(1L);
        PresetEntryRequest presetEntryRequest2 = new PresetEntryRequest();
        presetEntryRequest2.setCategory(2L);
        presetRequest.setPresetEntries(List.of(presetEntryRequest1,presetEntryRequest2));
        given(prizeCategoryService.getCategoryById(1L)).willReturn(Optional.of(category1));
        given(prizeCategoryService.getCategoryById(2L)).willReturn(Optional.of(category2));


        //when
        Preset actualPreset =  underTest.convertToPresetEntity(presetRequest);

        //then
        assertNotNull(actualPreset);
        assertEquals(presetName, actualPreset.getName());
        assertEquals(category1, actualPreset.getPresetEntries().get(0).getPrizeCategory());
        assertEquals(category2, actualPreset.getPresetEntries().get(1).getPrizeCategory());
    }
    @Test
    void convertToPresetEntityThrowsEntityNotFoundException()
    {
        //given
        String presetName = "preset1";
        PresetRequest presetRequest = new PresetRequest();
        PresetEntryRequest presetEntryRequest1 = new PresetEntryRequest();
        presetEntryRequest1.setCategory(1L);
        presetRequest.setPresetEntries(List.of(presetEntryRequest1));
        given(prizeCategoryService.getCategoryById(1L)).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> underTest.convertToPresetEntity(presetRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }
}