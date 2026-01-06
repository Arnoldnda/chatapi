

/*
 * Java transformer for entity table type_message 
 * Created on 2026-01-03 ( Time 10:00:04 )
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
 * TRANSFORMER for table "type_message"
 * 
 * @author Geo
 *
 */
@Mapper
public interface TypeMessageTransformer {

	TypeMessageTransformer INSTANCE = Mappers.getMapper(TypeMessageTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
	})
	TypeMessageDto toDto(TypeMessage entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<TypeMessageDto> toDtos(List<TypeMessage> entities) throws ParseException;

    default TypeMessageDto toLiteDto(TypeMessage entity) {
		if (entity == null) {
			return null;
		}
		TypeMessageDto dto = new TypeMessageDto();
		dto.setId( entity.getId() );
		dto.setLibelle( entity.getLibelle() );
		return dto;
    }

	default List<TypeMessageDto> toLiteDtos(List<TypeMessage> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<TypeMessageDto> dtos = new ArrayList<TypeMessageDto>();
		for (TypeMessage entity : entities) {
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
    TypeMessage toEntity(TypeMessageDto dto) throws ParseException;

    //List<TypeMessage> toEntities(List<TypeMessageDto> dtos) throws ParseException;

}
