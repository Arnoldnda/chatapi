

/*
 * Java transformer for entity table conversation 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.dto.transformer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.dao.entity.*;


/**
 * TRANSFORMER for table "conversation"
 * 
 * @author Geo
 *
 */
@Mapper
public interface ConversationTransformer {

	ConversationTransformer INSTANCE = Mappers.getMapper(ConversationTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="entity.typeConversation.id", target="typeConversationId"),
		@Mapping(source="entity.typeConversation.libelle", target="typeConversationLibelle"),
		@Mapping(source="entity.typeConversation.code", target="typeConversationCode"),
	})
	ConversationDto toDto(Conversation entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ConversationDto> toDtos(List<Conversation> entities) throws ParseException;

    default ConversationDto toLiteDto(Conversation entity) {
		if (entity == null) {
			return null;
		}
		ConversationDto dto = new ConversationDto();
		dto.setId( entity.getId() );
		return dto;
    }

	default List<ConversationDto> toLiteDtos(List<Conversation> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<ConversationDto> dtos = new ArrayList<ConversationDto>();
		for (Conversation entity : entities) {
			dtos.add(toLiteDto(entity));
		}
		return dtos;
	}

	@Mappings({
		@Mapping(source="dto.id", target="id"),
		@Mapping(source="dto.titre", target="titre"),
		@Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="dto.createdBy", target="createdBy"),
		@Mapping(source="dto.updatedBy", target="updatedBy"),
		@Mapping(source="dto.deletedBy", target="deletedBy"),
		@Mapping(source="dto.isDeleted", target="isDeleted"),
		@Mapping(source="typeConversation", target="typeConversation"),
	})
    Conversation toEntity(ConversationDto dto, TypeConversation typeConversation) throws ParseException;

    //List<Conversation> toEntities(List<ConversationDto> dtos) throws ParseException;

}
