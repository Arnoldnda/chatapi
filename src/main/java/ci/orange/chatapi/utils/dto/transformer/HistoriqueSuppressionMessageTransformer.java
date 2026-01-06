

/*
 * Java transformer for entity table historique_suppression_message 
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
 * TRANSFORMER for table "historique_suppression_message"
 * 
 * @author Geo
 *
 */
@Mapper
public interface HistoriqueSuppressionMessageTransformer {

	HistoriqueSuppressionMessageTransformer INSTANCE = Mappers.getMapper(HistoriqueSuppressionMessageTransformer.class);

	@FullTransformerQualifier
	@Mappings({
		@Mapping(source="entity.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="entity.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="entity.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="entity.user.id", target="userId"),
		@Mapping(source="entity.user.nom", target="userNom"),
		@Mapping(source="entity.user.prenoms", target="userPrenoms"),
		@Mapping(source="entity.user.login", target="userLogin"),
		@Mapping(source="entity.message.id", target="messageId"),
	})
	HistoriqueSuppressionMessageDto toDto(HistoriqueSuppressionMessage entity) throws ParseException;

	@IterableMapping(qualifiedBy = {FullTransformerQualifier.class})
    List<HistoriqueSuppressionMessageDto> toDtos(List<HistoriqueSuppressionMessage> entities) throws ParseException;

    default HistoriqueSuppressionMessageDto toLiteDto(HistoriqueSuppressionMessage entity) {
		if (entity == null) {
			return null;
		}
		HistoriqueSuppressionMessageDto dto = new HistoriqueSuppressionMessageDto();
		dto.setId( entity.getId() );
		return dto;
    }

	default List<HistoriqueSuppressionMessageDto> toLiteDtos(List<HistoriqueSuppressionMessage> entities) {
		if (entities == null || entities.stream().allMatch(o -> o == null)) {
			return null;
		}
		List<HistoriqueSuppressionMessageDto> dtos = new ArrayList<HistoriqueSuppressionMessageDto>();
		for (HistoriqueSuppressionMessage entity : entities) {
			dtos.add(toLiteDto(entity));
		}
		return dtos;
	}

	@Mappings({
		@Mapping(source="dto.id", target="id"),
		@Mapping(source="dto.isHidden", target="isHidden"),
		@Mapping(source="dto.createdAt", dateFormat="dd/MM/yyyy",target="createdAt"),
		@Mapping(source="dto.updatedAt", dateFormat="dd/MM/yyyy",target="updatedAt"),
		@Mapping(source="dto.deletedAt", dateFormat="dd/MM/yyyy",target="deletedAt"),
		@Mapping(source="dto.createdBy", target="createdBy"),
		@Mapping(source="dto.updatedBy", target="updatedBy"),
		@Mapping(source="dto.deletedBy", target="deletedBy"),
		@Mapping(source="dto.isDeleted", target="isDeleted"),
		@Mapping(source="user", target="user"),
		@Mapping(source="message", target="message"),
	})
    HistoriqueSuppressionMessage toEntity(HistoriqueSuppressionMessageDto dto, User user, Message message) throws ParseException;

    //List<HistoriqueSuppressionMessage> toEntities(List<HistoriqueSuppressionMessageDto> dtos) throws ParseException;

}
