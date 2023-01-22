package com.bingo.util.mapper;

import com.bingo.api.request.PlayerRequest;
import com.bingo.api.response.PlayerResponse;
import com.bingo.domain.document.PlayerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    @Mapping(target = "id", ignore = true)
    PlayerDocument toPlayerDocument(final PlayerRequest playerRequest);

    PlayerResponse toPlayerResponse(final PlayerDocument playerDocument);
}
