                                                                                        																				
/*
 * Java business for entity table conversation_user 
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
import ci.orange.chatapi.dao.entity.ConversationUser;
import ci.orange.chatapi.dao.entity.User;
import ci.orange.chatapi.dao.entity.Conversation;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;

/**
BUSINESS for table "conversation_user"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class ConversationUserBusiness implements IBasicBusiness<Request<ConversationUserDto>, Response<ConversationUserDto>> {

	private Response<ConversationUserDto> response;
	@Autowired
	private ConversationUserRepository conversationUserRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConversationRepository conversationRepository;
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

	public ConversationUserBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	/**
	 * create ConversationUser by using ConversationUserDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationUserDto> create(Request<ConversationUserDto> request, Locale locale)  throws ParseException {
		log.info("----begin create ConversationUser-----");

		Response<ConversationUserDto> response = new Response<ConversationUserDto>();
		List<ConversationUser>        items    = new ArrayList<ConversationUser>();
			
		for (ConversationUserDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("conversationId", dto.getConversationId());
			fieldsToVerify.put("userId", dto.getUserId());
            fieldsToVerify.put("role", dto.getRole());

//			fieldsToVerify.put("hasLeft", dto.getHasLeft());
//			fieldsToVerify.put("leftAt", dto.getLeftAt());
//			fieldsToVerify.put("leftBy", dto.getLeftBy());
//			fieldsToVerify.put("hasDefinitivelyLeft", dto.getHasDefinitivelyLeft());
//			fieldsToVerify.put("definitivelyLeftAt", dto.getDefinitivelyLeftAt());
//			fieldsToVerify.put("definitivelyLeftBy", dto.getDefinitivelyLeftBy());
//			fieldsToVerify.put("recreatedAt", dto.getRecreatedAt());
//			fieldsToVerify.put("recreatedBy", dto.getRecreatedBy());
//			fieldsToVerify.put("hasCleaned", dto.getHasCleaned());
//			fieldsToVerify.put("deletedAt", dto.getDeletedAt());
//			fieldsToVerify.put("deletedBy", dto.getDeletedBy());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if conversationUser to insert do not exist
			ConversationUser existingEntity = null;

/*
			if (existingEntity != null) {
				response.setStatus(functionalError.DATA_EXIST("conversationUser id -> " + dto.getId(), locale));
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
			// Verify if conversation exist
			Conversation existingConversation = null;
			if (dto.getConversationId() != null && dto.getConversationId() > 0){
				existingConversation = conversationRepository.findOne(dto.getConversationId(), false);
				if (existingConversation == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("conversation conversationId -> " + dto.getConversationId(), locale));
					response.setHasError(true);
					return response;
				}
			}
				ConversationUser entityToSave = null;
			entityToSave = ConversationUserTransformer.INSTANCE.toEntity(dto, existingUser, existingConversation);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<ConversationUser> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = conversationUserRepository.saveAll((Iterable<ConversationUser>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversationUser", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationUserDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationUserTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationUserTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end create ConversationUser-----");
		return response;
	}

	/**
	 * update ConversationUser by using ConversationUserDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationUserDto> update(Request<ConversationUserDto> request, Locale locale)  throws ParseException {
		log.info("----begin update ConversationUser-----");

		Response<ConversationUserDto> response = new Response<ConversationUserDto>();
		List<ConversationUser>        items    = new ArrayList<ConversationUser>();
			
		for (ConversationUserDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversationUser existe
			ConversationUser entityToSave = null;
			entityToSave = conversationUserRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversationUser id -> " + dto.getId(), locale));
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
			// Verify if conversation exist
			if (dto.getConversationId() != null && dto.getConversationId() > 0){
				Conversation existingConversation = conversationRepository.findOne(dto.getConversationId(), false);
				if (existingConversation == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("conversation conversationId -> " + dto.getConversationId(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setConversation(existingConversation);
			}
			if (dto.getHasLeft() != null) {
				entityToSave.setHasLeft(dto.getHasLeft());
			}
			if (Utilities.notBlank(dto.getLeftAt())) {
				entityToSave.setLeftAt(dateFormat.parse(dto.getLeftAt()));
			}
			if (dto.getLeftBy() != null && dto.getLeftBy() > 0) {
				entityToSave.setLeftBy(dto.getLeftBy());
			}
			if (dto.getHasDefinitivelyLeft() != null) {
				entityToSave.setHasDefinitivelyLeft(dto.getHasDefinitivelyLeft());
			}
			if (Utilities.notBlank(dto.getDefinitivelyLeftAt())) {
				entityToSave.setDefinitivelyLeftAt(dateFormat.parse(dto.getDefinitivelyLeftAt()));
			}
			if (dto.getDefinitivelyLeftBy() != null && dto.getDefinitivelyLeftBy() > 0) {
				entityToSave.setDefinitivelyLeftBy(dto.getDefinitivelyLeftBy());
			}
			if (Utilities.notBlank(dto.getRecreatedAt())) {
				entityToSave.setRecreatedAt(dateFormat.parse(dto.getRecreatedAt()));
			}
			if (dto.getRecreatedBy() != null && dto.getRecreatedBy() > 0) {
				entityToSave.setRecreatedBy(dto.getRecreatedBy());
			}
			if (dto.getHasCleaned() != null) {
				entityToSave.setHasCleaned(dto.getHasCleaned());
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
			List<ConversationUser> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = conversationUserRepository.saveAll((Iterable<ConversationUser>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversationUser", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationUserDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationUserTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationUserTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end update ConversationUser-----");
		return response;
	}

	/**
	 * delete ConversationUser by using ConversationUserDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationUserDto> delete(Request<ConversationUserDto> request, Locale locale)  {
		log.info("----begin delete ConversationUser-----");

		Response<ConversationUserDto> response = new Response<ConversationUserDto>();
		List<ConversationUser>        items    = new ArrayList<ConversationUser>();
			
		for (ConversationUserDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversationUser existe
			ConversationUser existingEntity = null;

			existingEntity = conversationUserRepository.findOne(dto.getId(), false);
			if (existingEntity == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversationUser -> " + dto.getId(), locale));
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
			conversationUserRepository.saveAll((Iterable<ConversationUser>) items);

			response.setHasError(false);
		}

		log.info("----end delete ConversationUser-----");
		return response;
	}

	/**
	 * get ConversationUser by using ConversationUserDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationUserDto> getByCriteria(Request<ConversationUserDto> request, Locale locale)  throws Exception {
		log.info("----begin get ConversationUser-----");

		Response<ConversationUserDto> response = new Response<ConversationUserDto>();
		List<ConversationUser> items 			 = conversationUserRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<ConversationUserDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationUserTransformer.INSTANCE.toLiteDtos(items) : ConversationUserTransformer.INSTANCE.toDtos(items);

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
			response.setCount(conversationUserRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("conversationUser", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get ConversationUser-----");
		return response;
	}

	/**
	 * get full ConversationUserDto by using ConversationUser as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ConversationUserDto getFullInfos(ConversationUserDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
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
