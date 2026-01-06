                                                        												
/*
 * Java business for entity table historique_suppression_message 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.business;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.enums.*;
import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.contract.IBasicBusiness;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.dto.transformer.*;
import ci.orange.chatapi.dao.entity.HistoriqueSuppressionMessage;
import ci.orange.chatapi.dao.entity.User;
import ci.orange.chatapi.dao.entity.Message;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;

/**
BUSINESS for table "historique_suppression_message"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class HistoriqueSuppressionMessageBusiness implements IBasicBusiness<Request<HistoriqueSuppressionMessageDto>, Response<HistoriqueSuppressionMessageDto>> {

	private Response<HistoriqueSuppressionMessageDto> response;
	@Autowired
	private HistoriqueSuppressionMessageRepository historiqueSuppressionMessageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private FunctionalError functionalError;
	@Autowired
	private TechnicalError technicalError;
	@Autowired
	private ExceptionUtils exceptionUtils;
	@PersistenceContext
	private EntityManager em;

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateTimeFormat;

	public HistoriqueSuppressionMessageBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	/**
	 * create HistoriqueSuppressionMessage by using HistoriqueSuppressionMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<HistoriqueSuppressionMessageDto> create(Request<HistoriqueSuppressionMessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin create HistoriqueSuppressionMessage-----");

		Response<HistoriqueSuppressionMessageDto> response = new Response<HistoriqueSuppressionMessageDto>();
		List<HistoriqueSuppressionMessage>        items    = new ArrayList<HistoriqueSuppressionMessage>();
			
		for (HistoriqueSuppressionMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("messageId", dto.getMessageId());
			fieldsToVerify.put("userId", dto.getUserId());
			fieldsToVerify.put("isHidden", dto.getIsHidden());
			fieldsToVerify.put("deletedAt", dto.getDeletedAt());
			fieldsToVerify.put("deletedBy", dto.getDeletedBy());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if historiqueSuppressionMessage to insert do not exist
			HistoriqueSuppressionMessage existingEntity = null;

/*
			if (existingEntity != null) {
				response.setStatus(functionalError.DATA_EXIST("historiqueSuppressionMessage id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

*/
			// Verify if user exist
			User existingUser = null;
			if (dto.getUserId() != null && dto.getUserId() > 0){
				existingUser = userRepository.findOne(dto.getUserId(), false);
				if (existingUser == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("user userId -> " + dto.getUserId(), locale));
					response.setHasError(true);
					return response;
				}
			}
			// Verify if message exist
			Message existingMessage = null;
			if (dto.getMessageId() != null && dto.getMessageId() > 0){
				existingMessage = messageRepository.findOne(dto.getMessageId(), false);
				if (existingMessage == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("message messageId -> " + dto.getMessageId(), locale));
					response.setHasError(true);
					return response;
				}
			}
				HistoriqueSuppressionMessage entityToSave = null;
			entityToSave = HistoriqueSuppressionMessageTransformer.INSTANCE.toEntity(dto, existingUser, existingMessage);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<HistoriqueSuppressionMessage> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = historiqueSuppressionMessageRepository.saveAll((Iterable<HistoriqueSuppressionMessage>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("historiqueSuppressionMessage", locale));
				response.setHasError(true);
				return response;
			}
			List<HistoriqueSuppressionMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? HistoriqueSuppressionMessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : HistoriqueSuppressionMessageTransformer.INSTANCE.toDtos(itemsSaved);

			final int size = itemsSaved.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setHasError(false);
		}

		log.info("----end create HistoriqueSuppressionMessage-----");
		return response;
	}

	/**
	 * update HistoriqueSuppressionMessage by using HistoriqueSuppressionMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<HistoriqueSuppressionMessageDto> update(Request<HistoriqueSuppressionMessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin update HistoriqueSuppressionMessage-----");

		Response<HistoriqueSuppressionMessageDto> response = new Response<HistoriqueSuppressionMessageDto>();
		List<HistoriqueSuppressionMessage>        items    = new ArrayList<HistoriqueSuppressionMessage>();
			
		for (HistoriqueSuppressionMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la historiqueSuppressionMessage existe
			HistoriqueSuppressionMessage entityToSave = null;
			entityToSave = historiqueSuppressionMessageRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("historiqueSuppressionMessage id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if user exist
			if (dto.getUserId() != null && dto.getUserId() > 0){
				User existingUser = userRepository.findOne(dto.getUserId(), false);
				if (existingUser == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("user userId -> " + dto.getUserId(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setUser(existingUser);
			}
			// Verify if message exist
			if (dto.getMessageId() != null && dto.getMessageId() > 0){
				Message existingMessage = messageRepository.findOne(dto.getMessageId(), false);
				if (existingMessage == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("message messageId -> " + dto.getMessageId(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setMessage(existingMessage);
			}
			if (dto.getIsHidden() != null) {
				entityToSave.setIsHidden(dto.getIsHidden());
			}
			if (Utilities.notBlank(dto.getDeletedAt())) {
				entityToSave.setDeletedAt(dateFormat.parse(dto.getDeletedAt()));
			}
			if (dto.getCreatedBy() != null && dto.getCreatedBy() > 0) {
				entityToSave.setCreatedBy(dto.getCreatedBy());
			}
			if (dto.getUpdatedBy() != null && dto.getUpdatedBy() > 0) {
				entityToSave.setUpdatedBy(dto.getUpdatedBy());
			}
			if (dto.getDeletedBy() != null && dto.getDeletedBy() > 0) {
				entityToSave.setDeletedBy(dto.getDeletedBy());
			}
			entityToSave.setUpdatedAt(Utilities.getCurrentDate());
			entityToSave.setUpdatedBy(request.getUser());
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<HistoriqueSuppressionMessage> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = historiqueSuppressionMessageRepository.saveAll((Iterable<HistoriqueSuppressionMessage>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("historiqueSuppressionMessage", locale));
				response.setHasError(true);
				return response;
			}
			List<HistoriqueSuppressionMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? HistoriqueSuppressionMessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : HistoriqueSuppressionMessageTransformer.INSTANCE.toDtos(itemsSaved);

			final int size = itemsSaved.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setHasError(false);
		}

		log.info("----end update HistoriqueSuppressionMessage-----");
		return response;
	}

	/**
	 * delete HistoriqueSuppressionMessage by using HistoriqueSuppressionMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<HistoriqueSuppressionMessageDto> delete(Request<HistoriqueSuppressionMessageDto> request, Locale locale)  {
		log.info("----begin delete HistoriqueSuppressionMessage-----");

		Response<HistoriqueSuppressionMessageDto> response = new Response<HistoriqueSuppressionMessageDto>();
		List<HistoriqueSuppressionMessage>        items    = new ArrayList<HistoriqueSuppressionMessage>();
			
		for (HistoriqueSuppressionMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la historiqueSuppressionMessage existe
			HistoriqueSuppressionMessage existingEntity = null;

			existingEntity = historiqueSuppressionMessageRepository.findOne(dto.getId(), false);
			if (existingEntity == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("historiqueSuppressionMessage -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// -----------------------------------------------------------------------
			// ----------- CHECK IF DATA IS USED
			// -----------------------------------------------------------------------



			existingEntity.setDeletedAt(Utilities.getCurrentDate());
			existingEntity.setDeletedBy(request.getUser());
			existingEntity.setIsDeleted(true);
			items.add(existingEntity);
		}

		if (!items.isEmpty()) {
			// supprimer les donnees en base
			historiqueSuppressionMessageRepository.saveAll((Iterable<HistoriqueSuppressionMessage>) items);

			response.setHasError(false);
		}

		log.info("----end delete HistoriqueSuppressionMessage-----");
		return response;
	}

	/**
	 * get HistoriqueSuppressionMessage by using HistoriqueSuppressionMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<HistoriqueSuppressionMessageDto> getByCriteria(Request<HistoriqueSuppressionMessageDto> request, Locale locale)  throws Exception {
		log.info("----begin get HistoriqueSuppressionMessage-----");

		Response<HistoriqueSuppressionMessageDto> response = new Response<HistoriqueSuppressionMessageDto>();
		List<HistoriqueSuppressionMessage> items 			 = historiqueSuppressionMessageRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<HistoriqueSuppressionMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? HistoriqueSuppressionMessageTransformer.INSTANCE.toLiteDtos(items) : HistoriqueSuppressionMessageTransformer.INSTANCE.toDtos(items);

			final int size = items.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setCount(historiqueSuppressionMessageRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("historiqueSuppressionMessage", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get HistoriqueSuppressionMessage-----");
		return response;
	}

	/**
	 * get full HistoriqueSuppressionMessageDto by using HistoriqueSuppressionMessage as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private HistoriqueSuppressionMessageDto getFullInfos(HistoriqueSuppressionMessageDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
		// put code here

		if (Utilities.isTrue(isSimpleLoading)) {
			return dto;
		}
		if (size > 1) {
			return dto;
		}

		return dto;
	}
}
