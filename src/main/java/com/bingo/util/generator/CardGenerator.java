package com.bingo.util.generator;

import com.bingo.domain.document.CardDocument;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CardGenerator {
    public final static int MIN_NUMBER = 0;
    public final static int MAX_NUMBER = 99;
    public final static int CARD_SIZE = 20;
    public final static int ROUND_SIZE = 100;
    public final static int MAX_EQUAL_NUMBERS = 5;
    public final static int HITS_TO_WIN = 5;
    private final Random random = new Random();

    public CardDocument newCard(final List<CardDocument> currentCards) {
        var allCurrentNumbers = currentCards.stream()
                .map(CardDocument::numbers)
                .toList();

        var newNumbers = new LinkedHashSet<Integer>();

        while (newNumbers.size() < CARD_SIZE) {
            var n = random.nextInt(MIN_NUMBER, MAX_NUMBER + 1);
            newNumbers.add(n);

            if (!allNumbersAreValid(allCurrentNumbers, newNumbers))
                newNumbers.remove(n);
        }

        return CardDocument.builder()
                .numbers(newNumbers)
                .build();
    }

    private boolean allNumbersAreValid(
            final List<LinkedHashSet<Integer>> allCurrentNumbers,
            final LinkedHashSet<Integer> newNumbers) {

        for (var currentNumbers : allCurrentNumbers) {
            if (!numbersAreValid(currentNumbers, newNumbers))
                return false;
        }

        return true;
    }

    private boolean numbersAreValid(
            final LinkedHashSet<Integer> currentNumbers,
            final LinkedHashSet<Integer> newNumbers) {

        var t = 0;

        for (var number : newNumbers) {
            if (currentNumbers.contains(number)) {
                t++;

                if (t > MAX_EQUAL_NUMBERS)
                    return false;
            }
        }

        return true;
    }
}
