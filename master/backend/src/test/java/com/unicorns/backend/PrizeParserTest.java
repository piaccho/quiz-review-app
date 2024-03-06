package com.unicorns.backend;
import com.unicorns.backend.model.Prize;
import com.unicorns.backend.utill.PrizeParser;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrizeParserTest {

    @Autowired
    private PrizeParser prizeParser;

    // You can mock PrizeService if needed and inject it into PrizeParser

    @Test
    public void testParsePrizesFromString() {
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