package com.unicorns.backend.utill;

import com.unicorns.backend.model.Prize;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class PrizeParserTest {

    @Autowired
    private PrizeParser prizeParser;

    @Test
    void ParseEmptyString() {
        List<Prize> result = prizeParser.parsePrizesFromString("");
        assertTrue(result.isEmpty());
    }
    @Test
    public void ParsePrizesFromString() {
        // Given
        String prizeString = "Marchewka lab (+10% do lab);Rabat na sianko (darmowa kartkówka);Lekarstwo (odrobienie 2pkt lab);Weterynarz (odrobienie 1 kartkówki);";

        // When
        List<Prize> parsedPrizes = prizeParser.parsePrizesFromString(prizeString);
        System.out.println(parsedPrizes);

        // Then
        assertEquals(4, parsedPrizes.size());
        assertEquals("Marchewka lab", parsedPrizes.get(0).getName());
        assertEquals("darmowa kartkówka", parsedPrizes.get(1).getDescription());
    }
}