                                                    											
/*
 * Java business for entity table type_message 
 * Created on 2026-01-03 ( Time 10:00:04 )
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
import ci.orange.chatapi.dao.entity.TypeMessage;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;

/**
BUSINESS for table "type_message"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class TypeMessageBusiness implements IBasicBusiness<Request<TypeMessageDto>, Response<TypeMessageDto>> {

	private Response<TypeMessageDto> response;
	@Autowired
	private TypeMessageRepository typeMessageRepository;
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

	public TypeMessageBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	/**
	 * create TypeMessage by using TypeMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<TypeMessageDto> create(Request<TypeMessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin create TypeMessage-----");

		Response<TypeMessageDto> response = new Response<TypeMessageDto>();
		List<TypeMessage>        items    = new ArrayList<TypeMessage>();
			
		for (TypeMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("libelle", dto.getLibelle());
			fieldsToVerify.put("code", dto.getCode());
			fieldsToVerify.put("deletedAt", dto.getDeletedAt());
			fieldsToVerify.put("deletedBy", dto.getDeletedBy());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if typeMessage to insert do not exist
			TypeMessage existingEntity = null;

/*
			if (existingEntity != null) {
				response.setStatus(functionalError.DATA_EXIST("typeMessage id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

*/
			// verif unique libelle in db
			existingEntity = typeMessageRepository.findByLibelle(dto.getLibelle(), false);
			if (existingEntity != null) {
				response.setStatus(functionalError.DATA_EXIST("typeMessage libelle -> " + dto.getLibelle(), locale));
				response.setHasError(true);
				return response;
			}
			// verif unique libelle in items to save
			if (items.stream().anyMatch(a -> a.getLibelle().equalsIgnoreCase(dto.getLibelle()))) {
				response.setStatus(functionalError.DATA_DUPLICATE(" libelle ", locale));
				response.setHasError(true);
				return response;
			}

			// verif unique code in db
			existingEntity = typeMessageRepository.findByCode(dto.getCode(), false);
			if (existingEntity != null) {
				response.setStatus(functionalError.DATA_EXIST("typeMessage code -> " + dto.getCode(), locale));
				response.setHasError(true);
				return response;
			}
			// verif unique code in items to save
			if (items.stream().anyMatch(a -> a.getCode().equalsIgnoreCase(dto.getCode()))) {
				response.setStatus(functionalError.DATA_DUPLICATE(" code ", locale));
				response.setHasError(true);
				return response;
			}

				TypeMessage entityToSave = null;
			entityToSave = TypeMessageTransformer.INSTANCE.toEntity(dto);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<TypeMessage> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = typeMessageRepository.saveAll((Iterable<TypeMessage>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("typeMessage", locale));
				response.setHasError(true);
				return response;
			}
			List<TypeMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? TypeMessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : TypeMessageTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end create TypeMessage-----");
		return response;
	}

	/**
	 * update TypeMessage by using TypeMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<TypeMessageDto> update(Request<TypeMessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin update TypeMessage-----");

		Response<TypeMessageDto> response = new Response<TypeMessageDto>();
		List<TypeMessage>        items    = new ArrayList<TypeMessage>();
			
		for (TypeMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la typeMessage existe
			TypeMessage entityToSave = null;
			entityToSave = typeMessageRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("typeMessage id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			if (Utilities.notBlank(dto.getLibelle())) {
				entityToSave.setLibelle(dto.getLibelle());
			}
			if (Utilities.notBlank(dto.getCode())) {
				entityToSave.setCode(dto.getCode());
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
			List<TypeMessage> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = typeMessageRepository.saveAll((Iterable<TypeMessage>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("typeMessage", locale));
				response.setHasError(true);
				return response;
			}
			List<TypeMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? TypeMessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : TypeMessageTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end update TypeMessage-----");
		return response;
	}

	/**
	 * delete TypeMessage by using TypeMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<TypeMessageDto> delete(Request<TypeMessageDto> request, Locale locale)  {
		log.info("----begin delete TypeMessage-----");

		Response<TypeMessageDto> response = new Response<TypeMessageDto>();
		List<TypeMessage>        items    = new ArrayList<TypeMessage>();
			
		for (TypeMessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la typeMessage existe
			TypeMessage existingEntity = null;

			existingEntity = typeMessageRepository.findOne(dto.getId(), false);
			if (existingEntity == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("typeMessage -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// -----------------------------------------------------------------------
			// ----------- CHECK IF DATA IS USED
			// -----------------------------------------------------------------------

			// message
			List<Message> listOfMessage = messageRepository.findByTypeMessage(existingEntity.getId(), false);
			if (listOfMessage != null && !listOfMessage.isEmpty()){
				response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfMessage.size() + ")", locale));
				response.setHasError(true);
				return response;
			}


			existingEntity.setDeletedAt(Utilities.getCurrentDate());
			existingEntity.setDeletedBy(request.getUser());
			existingEntity.setIsDeleted(true);
			items.add(existingEntity);
		}

		if (!items.isEmpty()) {
			// supprimer les donnees en base
			typeMessageRepository.saveAll((Iterable<TypeMessage>) items);

			response.setHasError(false);
		}

		log.info("----end delete TypeMessage-----");
		return response;
	}

	/**
	 * get TypeMessage by using TypeMessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<TypeMessageDto> getByCriteria(Request<TypeMessageDto> request, Locale locale)  throws Exception {
		log.info("----begin get TypeMessage-----");

		Response<TypeMessageDto> response = new Response<TypeMessageDto>();
		List<TypeMessage> items 			 = typeMessageRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<TypeMessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? TypeMessageTransformer.INSTANCE.toLiteDtos(items) : TypeMessageTransformer.INSTANCE.toDtos(items);

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
			response.setCount(typeMessageRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("typeMessage", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get TypeMessage-----");
		return response;
	}

	/**
	 * get full TypeMessageDto by using TypeMessage as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private TypeMessageDto getFullInfos(TypeMessageDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
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
