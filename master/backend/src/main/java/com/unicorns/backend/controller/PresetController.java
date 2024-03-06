package com.unicorns.backend.controller;

import com.unicorns.backend.DTO.PresetDescriptorDTO;
import com.unicorns.backend.model.Preset;
import com.unicorns.backend.request.PresetRequest;
import com.unicorns.backend.service.PresetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/presets")
@RequiredArgsConstructor
public class PresetController {
    private final PresetService presetService;

    @PostMapping()
    public ResponseEntity<Long> createPrize(@RequestBody PresetRequest presetRequest) {

        Preset createdPreset = presetService.convertToPresetEntity(presetRequest);

        return presetService.addNewPreset(createdPreset).map(Preset::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(409).build());
    }

    @GetMapping("{id}")
    public ResponseEntity<Preset> getPresetById(@PathVariable(name = "id") long id) {
        return presetService.getPresetById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping()
    public List<Preset> getAllPresets() {
        return presetService.getAllPresets();

    }

    @GetMapping("all-descriptors")
    public List<PresetDescriptorDTO> getAllPresetDescriptors() {
        List<Preset> allPresets = presetService.getAllPresets();

        return allPresets.stream()
                .map(this::mapToDescriptorDTO)
                .collect(Collectors.toList());
    }

    private PresetDescriptorDTO mapToDescriptorDTO(Preset preset) {
        return new PresetDescriptorDTO(preset.getId(), preset.getName());
    }
}
