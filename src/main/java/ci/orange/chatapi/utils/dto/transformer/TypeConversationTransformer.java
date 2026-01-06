

/*
 * Java transformer for entity table type_conversation 
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
 * TRANSFORMER for table "type_conversation"
 * 
 * @author Geo
 *
 */
@Mapper
public interface TypeConversationTransformer {

	TypeConversationTransformer INSTANCE = Mappers.getMapper(TypeConversationTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
	})
	TypeConversationDto toDto(TypeConversation entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<TypeConversationDto> toDtos(List<TypeConversation> entities) throws ParseException;

    default TypeConversationDto toLiteDto(TypeConversation entity) {
		if (entity == null) {
			return null;
		}
		TypeConversationDto dto = new TypeConversationDto();
		dto.setId( entity.getId() );
		dto.setLibelle( entity.getLibelle() );
		return dto;
    }

	default List<TypeConversationDto> toLiteDtos(List<TypeConversation> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<TypeConversationDto> dtos = new ArrayList<TypeConversationDto>();
		for (TypeConversation entity : entities) {
			dtos.add(toLiteDto(entity));
		}
		return dtos;
	}

	@Mappings({
		@Mapping(source="dto.id", target="id"),
		@Mapping(source="dto.libelle", target="libelle"),
		@Mapping(source="dto.code", target="code"),
		@Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="dto.createdBy", target="createdBy"),
		@Mapping(source="dto.updatedBy", target="updatedBy"),
		@Mapping(source="dto.deletedBy", target="deletedBy"),
		@Mapping(source="dto.isDeleted", target="isDeleted"),
	})
    TypeConversation toEntity(TypeConversationDto dto) throws ParseException;

    //List<TypeConversation> toEntities(List<TypeConversationDto> dtos) throws ParseException;

}
