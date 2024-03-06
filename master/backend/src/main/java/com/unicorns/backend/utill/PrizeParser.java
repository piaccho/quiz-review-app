package com.unicorns.backend.utill;

import com.unicorns.backend.model.Prize;
import com.unicorns.backend.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PrizeParser {

    private final PrizeService prizeService;

    @Autowired
    public PrizeParser(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    public  List<Prize> parsePrizesFromString(String prizeString) {
        List<Prize> preferredPrizes = new ArrayList<>();
        String[] prizesArray = prizeString.split(";");

        for (String prizeEntry : prizesArray) {
            String prizeName = prizeEntry.replaceAll("\\(.*\\)", "").trim();
            String prizeDescription = prizeEntry.replaceAll(".*\\((.*?)\\).*", "$1").trim();

            Optional<Prize> existingPrize = prizeService.checkIfPrizeExists(prizeName);

            if (existingPrize.isEmpty()) {
                Prize prizeToAdd = new Prize();
                prizeToAdd.setName(prizeName);
                prizeToAdd.setDescription(prizeDescription);
                prizeService.savePrize(prizeToAdd);
                preferredPrizes.add(prizeToAdd);
            }
            else {
                preferredPrizes.add(existingPrize.get());
            }
        }

        return preferredPrizes;
    }
}
