package com.unicorns.backend.service;

import com.unicorns.backend.model.Preset;
import com.unicorns.backend.model.PresetEntry;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.repository.PresetRepository;
import com.unicorns.backend.request.PresetRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresetService {

    private final PresetRepository presetRepository;
    private final PrizeCategoryService prizeCategoryService;

    @Autowired
    public PresetService(PresetRepository presetRepository, PrizeCategoryService prizeCategoryService) {
        this.presetRepository = presetRepository;
        this.prizeCategoryService = prizeCategoryService;
    }

    public Optional<Preset> getPresetById(long id) {
        return presetRepository.findById(id);
    }

    public List<Preset> getAllPresets() {
        return presetRepository.findAll();
    }

    public Preset savePreset(Preset preset) {
        return presetRepository.save(preset);
    }

    public Optional<Preset> checkIfPresetExists(String presetName) {
        List<Preset> allPresets = getAllPresets();

        return allPresets.stream()
                .filter(prize -> prize.getName().equalsIgnoreCase(presetName))
                .findFirst();
    }

    public Optional<Preset> addNewPreset(Preset presetToAdd) {

        if(checkIfPresetExists(presetToAdd.getName()).isEmpty()) {
            return Optional.of(savePreset(presetToAdd));
        }
        return Optional.empty();
    }

    public Preset convertToPresetEntity(PresetRequest presetRequest) {
        Preset preset = new Preset();
        preset.setName(presetRequest.getName());
        preset.setStrategy(presetRequest.getStrategy());

        List<PresetEntry> presetEntries = presetRequest.getPresetEntries().stream()
                .map(entryDTO -> {
                    PresetEntry entry = new PresetEntry();

                    entry.setValue(entryDTO.getValue());
                    PrizeCategory prizeCategory = prizeCategoryService.getCategoryById(entryDTO.getCategory())
                            .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + entryDTO.getCategory()));
                    entry.setPrizeCategory(prizeCategory);

                    return entry;
                })
                .collect(Collectors.toList());

        preset.setPresetEntries(presetEntries);
        return preset;
    }
}
