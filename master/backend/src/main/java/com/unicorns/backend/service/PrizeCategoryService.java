package com.unicorns.backend.service;

import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.model.Prize;
import com.unicorns.backend.repository.PrizeCategoryRepository;
import com.unicorns.backend.request.PrizeCategoryRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrizeCategoryService {


    private final PrizeCategoryRepository prizeCategoryRepository;
    private final PrizeService prizeService;

    @Autowired
    public PrizeCategoryService(PrizeCategoryRepository prizeCategoryRepository, PrizeService prizeService) {
        this.prizeCategoryRepository = prizeCategoryRepository;
        this.prizeService = prizeService;
    }

    public Optional<PrizeCategory> getCategoryById(long id)
    {
        return prizeCategoryRepository.findById(id);
    }
    public List<PrizeCategory> getAllCategories() {

        return prizeCategoryRepository.findAll();
    }

    public Optional <List<Prize>> getAvailablePrizes(long id)
    {
        return prizeCategoryRepository.findById(id).map(PrizeCategory::getAvailablePrizes);
    }

    public PrizeCategory saveCategory(PrizeCategory category) {
        return prizeCategoryRepository.save(category);
    }

    public Optional<PrizeCategory> checkIfCategoryExists(String categoryName) {
        List<PrizeCategory> allCategories = getAllCategories();

        return allCategories.stream()
                .filter(prize -> prize.getName().equalsIgnoreCase(categoryName))
                .findFirst();
    }

    public Optional<PrizeCategory> addNewCategory(PrizeCategory categoryToAdd) {
        if(checkIfCategoryExists(categoryToAdd.getName()).isEmpty()) {
            return Optional.of(saveCategory(categoryToAdd));
        }

        return Optional.empty();
    }

    public PrizeCategory convertToPrizeCategoryEntity(PrizeCategoryRequest prizeCategoryRequest) {
        PrizeCategory prizeCategory = new PrizeCategory();
        prizeCategory.setName(prizeCategoryRequest.getName());

        List<Prize> prizes = prizeCategoryRequest.getAvailablePrizes().stream()
                .map(entryDTO -> prizeService.getPrizeById(entryDTO)
                        .orElseThrow(() -> new EntityNotFoundException("Prize not found with ID: " + entryDTO))).toList();

        prizeCategory.setAvailablePrizes(prizes);

        return prizeCategory;
    }
}
