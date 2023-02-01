package com.bingo.domain.service;

import com.bingo.api.response.CardResponse;
import com.bingo.api.response.PageResponse;
import com.bingo.domain.document.CardDocument;
import com.bingo.domain.repository.CardRepository;
import com.bingo.domain.service.query.CardQueryService;
import com.bingo.util.mapper.CardMapper;
import com.bingo.util.mapper.CardMapperImpl;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardQueryService cardQueryService;
    private final CardMapper cardMapper = new CardMapperImpl();

    private CardService cardService;

    @BeforeEach
    void setup() {
        cardService = new CardService(cardRepository, cardQueryService, cardMapper);
    }

    @Test
    void findAllCards() {
        var total = 4;

        var cardsDocument = List.of(
                CardDocument.builder().id("1").build(),
                CardDocument.builder().id("2").build(),
                CardDocument.builder().id("3").build(),
                CardDocument.builder().id("4").build());

        var cardsRespose = List.of(
                CardResponse.builder().id("1").build(),
                CardResponse.builder().id("2").build(),
                CardResponse.builder().id("3").build(),
                CardResponse.builder().id("4").build());

        when(cardRepository.count()).thenReturn(Mono.just((long) total));
        when(cardRepository.findAll()).thenReturn(Flux.fromIterable(cardsDocument));

        StepVerifier.create(cardService.findAllCards(1, 5))
                .assertNext(page -> {
                    assertThat(page).hasNoNullFieldsOrProperties();
                    assertThat(page.page()).isEqualTo(1);
                    assertThat(page.limit()).isEqualTo(5);
                    assertThat(page.total()).isEqualTo(total);
                    assertThat(page.itens().size()).isEqualTo(4);
                    IntStream.range(0, page.itens().size()).forEach(i ->
                            assertThat((CardResponse) page.itens().get(i))
                                    .isEqualTo(cardsRespose.get(i))
                    );
                })
                .verifyComplete();

        StepVerifier.create(cardService.findAllCards(2, 3))
                .assertNext(page -> {
                    assertThat(page).hasNoNullFieldsOrProperties();
                    assertThat(page.page()).isEqualTo(2);
                    assertThat(page.limit()).isEqualTo(3);
                    assertThat(page.total()).isEqualTo(total);
                    assertThat(page.itens().size()).isEqualTo(1);
                    IntStream.range(3, page.itens().size()).forEach(i ->
                            assertThat((CardResponse) page.itens().get(i))
                                    .isEqualTo(cardsRespose.get(i))
                    );
                })
                .verifyComplete();
    }

    @Test
    void findCardByCardId() {
        var cardId = ObjectId.get().toString();
        var cardDocument = CardDocument.builder().id(cardId).build();
        var cardResponse = CardResponse.builder().id(cardId).build();

        when(cardQueryService.findCardByCardId(cardId)).thenReturn(Mono.just(cardDocument));

        StepVerifier.create(cardService.findCardByCardId(cardId))
                .assertNext(card -> {
                    assertThat(card).isEqualTo(cardResponse);
                })
                .verifyComplete();
    }

    @Test
    void findCardByPlayerId() {
    }

    @Test
    void findCardByRoundId() {
    }
}