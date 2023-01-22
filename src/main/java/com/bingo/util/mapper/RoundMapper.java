package com.bingo.util.mapper;

import com.bingo.api.response.RoundResponse;
import com.bingo.domain.document.RoundDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoundMapper {
    RoundResponse toRoundResponse(final RoundDocument roundDocument);
}
