package com.unicorns.backend.config;

import com.unicorns.backend.enums.Strategy;
import com.unicorns.backend.model.Preset;
import com.unicorns.backend.model.PresetEntry;
import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.model.Prize;
import com.unicorns.backend.repository.PresetRepository;
import com.unicorns.backend.repository.PrizeCategoryRepository;
import com.unicorns.backend.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;

import java.io.InputStream;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private PrizeCategoryRepository prizeCategoryRepository;
    @Autowired
    private PresetRepository presetRepository;

    @Override
    public void run(String... args) {
        Prize carrotLab = new Prize();
        carrotLab.setName("Marchewka lab");
        carrotLab.setDescription("+10% do sumy za lab z Javy (maks. 30%)");
        Prize carrotProject = new Prize();
        carrotProject.setName("Marchewka projektowa");
        carrotProject.setDescription("+10% do sumy za projekt (maks. 40%)");
        Prize medicine = new Prize();
        medicine.setName("Lekarstwo");
        medicine.setDescription("odrobienie straconych 2xp za lab");
        Prize vet = new Prize();
        vet.setName("Weterynarz");
        vet.setDescription("odrobienie straconych 2xp za kartkówkę");
        Prize hayDiscount = new Prize();
        hayDiscount.setName("Rabat na sianko");
        hayDiscount.setDescription("darmowe zaliczenie najbliższej kartkówki (2xp)");

        carrotLab = prizeRepository.save(carrotLab);
        carrotProject = prizeRepository.save(carrotProject);
        medicine = prizeRepository.save(medicine);
        vet = prizeRepository.save(vet);
        hayDiscount = prizeRepository.save(hayDiscount);

        PrizeCategory goldChest = new PrizeCategory();
        goldChest.setName("Złota Skrzynka");
        goldChest.setAvailablePrizes(Arrays.asList(medicine, carrotProject, carrotLab));
        PrizeCategory silverChest = new PrizeCategory();
        silverChest.setName("Srebrna Skrzynka");
        silverChest.setAvailablePrizes(Arrays.asList(vet, hayDiscount));
//        PrizeCategory bronzeChest = new PrizeCategory();
//        bronzeChest.setName("Brązowa Skrzynka");
//        bronzeChest.setAvailablePrizes(Arrays.asList(medicine, vet));
//
        prizeCategoryRepository.save(goldChest);
        prizeCategoryRepository.save(silverChest);
//        prizeCategoryRepository.save(bronzeChest);


        PresetEntry goldChestEntry = new PresetEntry();
        goldChestEntry.setValue(20);
        goldChestEntry.setPrizeCategory(goldChest);
        PresetEntry silverChestEntry = new PresetEntry();
        silverChestEntry.setValue(80);
        silverChestEntry.setPrizeCategory(silverChest);

        Preset preset = new Preset();
        preset.setName("Test preset - time");
        preset.setStrategy(Strategy.TIME);
        preset.setPresetEntries(Arrays.asList(
            goldChestEntry,
            silverChestEntry
        ));
        presetRepository.save(preset);
    }


}
