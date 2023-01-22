package com.bingo.util.generator;

import com.bingo.domain.document.RoundDocument;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.bingo.domain.document.RoundDocument.RoundStatus.*;
import static com.bingo.util.generator.CardGenerator.*;

@Component
public class RoundGenerator {
    private final Random random = new Random();

    public RoundDocument newRound() {
        return RoundDocument.builder()
                .status(OPEN)
                .numbers(new LinkedHashSet<>())
                .build();
    }

    public RoundDocument finishedRound(final RoundDocument roundDocument) {
        return RoundDocument.builder()
                .id(roundDocument.id())
                .status(FINISHED)
                .numbers(roundDocument.numbers())
                .createdAt(roundDocument.createdAt())
                .updatedAt(roundDocument.updatedAt())
                .build();
    }

    public RoundDocument addNewNumberToRound(final RoundDocument roundDocument) {
        if (roundDocument.numbers().size() >= ROUND_SIZE)
            return roundDocument;

        var numbers = new LinkedHashSet<>(roundDocument.numbers());
        var n = findNewNumber(numbers);

        if (n >= MIN_NUMBER && n <= MAX_NUMBER)
            numbers.add(n);

        return RoundDocument.builder()
                .id(roundDocument.id())
                .status(CLOSED)
                .numbers(numbers)
                .createdAt(roundDocument.createdAt())
                .updatedAt(roundDocument.updatedAt())
                .build();
    }

    private int findNewNumber(final LinkedHashSet<Integer> numbers) {
        if (numbers.size() >= ROUND_SIZE)
            return -1;

        var n = random.nextInt(MIN_NUMBER, MAX_NUMBER + 1);

        while (numbers.contains(n))
            n = random.nextInt(MIN_NUMBER, MAX_NUMBER + 1);

        return n;
    }
}
