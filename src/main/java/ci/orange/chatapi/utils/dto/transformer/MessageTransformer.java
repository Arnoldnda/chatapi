

/*
 * Java transformer for entity table message 
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
 * TRANSFORMER for table "message"
 * 
 * @author Geo
 *
 */
@Mapper
public interface MessageTransformer {

	MessageTransformer INSTANCE = Mappers.getMapper(MessageTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="entity.conversation.id", target="conversationId"),
		@Mapping(source="entity.typeMessage2.id", target="typeMessage"),
		@Mapping(source="entity.typeMessage2.libelle", target="typeMessageLibelle"),
		@Mapping(source="entity.typeMessage2.code", target="typeMessageCode"),
	})
	MessageDto toDto(Message entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<MessageDto> toDtos(List<Message> entities) throws ParseException;

    default MessageDto toLiteDto(Message entity) {
		if (entity == null) {
			return null;
		}
		MessageDto dto = new MessageDto();
		dto.setId( entity.getId() );
		return dto;
    }

	default List<MessageDto> toLiteDtos(List<Message> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<MessageDto> dtos = new ArrayList<MessageDto>();
		for (Message entity : entities) {
			dtos.add(toLiteDto(entity));
		}
		return dtos;
	}

	@Mappings({
		@Mapping(source="dto.id", target="id"),
		@Mapping(source="dto.content", target="content"),
		@Mapping(source="dto.imgUrl", target="imgUrl"),
		@Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="dto.createdBy", target="createdBy"),
		@Mapping(source="dto.updatedBy", target="updatedBy"),
		@Mapping(source="dto.deletedBy", target="deletedBy"),
		@Mapping(source="dto.isDeleted", target="isDeleted"),
		@Mapping(source="conversation", target="conversation"),
		@Mapping(source="typeMessage2", target="typeMessage2"),
	})
    Message toEntity(MessageDto dto, Conversation conversation, TypeMessage typeMessage2) throws ParseException;

    //List<Message> toEntities(List<MessageDto> dtos) throws ParseException;

}
