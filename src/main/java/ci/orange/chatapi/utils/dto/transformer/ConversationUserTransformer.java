

/*
 * Java transformer for entity table conversation_user 
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
 * TRANSFORMER for table "conversation_user"
 * 
 * @author Geo
 *
 */
@Mapper
public interface ConversationUserTransformer {

	ConversationUserTransformer INSTANCE = Mappers.getMapper(ConversationUserTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.leftAt", dateFormat="dd/MM/yyyy",target="leftAt"),
		@Mapping(source="entity.definitivelyLeftAt", dateFormat="dd/MM/yyyy",target="definitivelyLeftAt"),
		@Mapping(source="entity.recreatedAt", dateFormat="dd/MM/yyyy",target="recreatedAt"),
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="entity.user.id", target="userId"),
		@Mapping(source="entity.user.nom", target="userNom"),
		@Mapping(source="entity.user.prenoms", target="userPrenoms"),
		@Mapping(source="entity.user.login", target="userLogin"),
		@Mapping(source="entity.conversation.id", target="conversationId"),
	})
	ConversationUserDto toDto(ConversationUser entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<ConversationUserDto> toDtos(List<ConversationUser> entities) throws ParseException;

    default ConversationUserDto toLiteDto(ConversationUser entity) {
		if (entity == null) {
			return null;
		}
		ConversationUserDto dto = new ConversationUserDto();
		dto.setId( entity.getId() );
		return dto;
    }

	default List<ConversationUserDto> toLiteDtos(List<ConversationUser> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<ConversationUserDto> dtos = new ArrayList<ConversationUserDto>();
		for (ConversationUser entity : entities) {
			dtos.add(toLiteDto(entity));
		}
		return dtos;
	}

	@Mappings({
		@Mapping(source="dto.id", target="id"),
		@Mapping(source="dto.hasLeft", target="hasLeft"),
		@Mapping(source="dto.leftAt", dateFormat="dd/MM/yyyy",target="leftAt"),
		@Mapping(source="dto.leftBy", target="leftBy"),
		@Mapping(source="dto.hasDefinitivelyLeft", target="hasDefinitivelyLeft"),
		@Mapping(source="dto.definitivelyLeftAt", dateFormat="dd/MM/yyyy",target="definitivelyLeftAt"),
		@Mapping(source="dto.definitivelyLeftBy", target="definitivelyLeftBy"),
		@Mapping(source="dto.recreatedAt", dateFormat="dd/MM/yyyy",target="recreatedAt"),
		@Mapping(source="dto.recreatedBy", target="recreatedBy"),
		@Mapping(source="dto.hasCleaned", target="hasCleaned"),
		@Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="dto.createdBy", target="createdBy"),
		@Mapping(source="dto.updatedBy", target="updatedBy"),
		@Mapping(source="dto.deletedBy", target="deletedBy"),
		@Mapping(source="dto.isDeleted", target="isDeleted"),
		@Mapping(source="user", target="user"),
		@Mapping(source="conversation", target="conversation"),
	})
    ConversationUser toEntity(ConversationUserDto dto, User user, Conversation conversation) throws ParseException;

    //List<ConversationUser> toEntities(List<ConversationUserDto> dtos) throws ParseException;

}
