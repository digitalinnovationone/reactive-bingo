package com.bingo.util.mapper;

import com.bingo.api.response.CardResponse;
import com.bingo.domain.document.CardDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResponse toCardResponse(final CardDocument cardDocument);
}
