package com.unicorns.backend.controller;

import com.unicorns.backend.model.PrizeCategory;
import com.unicorns.backend.request.PrizeCategoryRequest;
import com.unicorns.backend.service.PrizeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class PrizeCategoryController {

    private final PrizeCategoryService prizeCategoryService;

    @PostMapping()
    public ResponseEntity<Long> createCategory(@RequestBody PrizeCategoryRequest prizeCategoryRequest) {

        PrizeCategory createdCategory = prizeCategoryService.convertToPrizeCategoryEntity(prizeCategoryRequest);
        return prizeCategoryService.addNewCategory(createdCategory).map(PrizeCategory::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(409).build());
    }

    @GetMapping()
    public List<PrizeCategory> getAllCategories() {
        return prizeCategoryService.getAllCategories();

    }

    @GetMapping("{id}")
    public ResponseEntity<PrizeCategory> getCategoryById(@PathVariable(name = "id") long id) {
        return prizeCategoryService.getCategoryById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

}
