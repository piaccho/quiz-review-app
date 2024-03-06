package com.unicorns.backend.service;


import com.unicorns.backend.model.Prize;
import com.unicorns.backend.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrizeService {

    private final PrizeRepository prizeRepository;


    @Autowired
    public PrizeService(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    public Optional<Prize> getPrizeById(long id) {
        return prizeRepository.findById(id);
    }

    public List<Prize> getAllPrizes()
    {
        return prizeRepository.findAll();
    }

    public Prize savePrize(Prize prize) {
        return prizeRepository.save(prize);
    }

    public Optional<Prize> checkIfPrizeExists(String prizeName) {
        List<Prize> allPrizes = getAllPrizes();

        return allPrizes.stream()
                .filter(prize -> prize.getName().equalsIgnoreCase(prizeName))
                .findFirst();
    }

    public Optional<Prize> addNewPrize(Prize prizeToAdd) {

        if(checkIfPrizeExists(prizeToAdd.getName()).isEmpty()) {
            return Optional.of(savePrize(prizeToAdd));
        }

        return Optional.empty();

    }
}
