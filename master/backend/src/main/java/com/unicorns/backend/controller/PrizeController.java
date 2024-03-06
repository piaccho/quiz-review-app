package com.unicorns.backend.controller;


import com.unicorns.backend.model.Prize;
import com.unicorns.backend.service.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prizes")
@RequiredArgsConstructor
public class PrizeController {

    private final PrizeService prizeService;

    @PostMapping()
    public ResponseEntity<Long> createPrize(@RequestBody Prize createdPrize) {
        return prizeService.addNewPrize(createdPrize).map(Prize::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(409).build());
    }

    @GetMapping("{id}")
    public ResponseEntity<Prize> getPrizeById(@PathVariable(name = "id") long id) {
        return prizeService.getPrizeById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping()
    public List<Prize> getAllPrize() {
        return prizeService.getAllPrizes();

    }








}
