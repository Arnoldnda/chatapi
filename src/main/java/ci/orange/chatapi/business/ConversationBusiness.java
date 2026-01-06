                                                    											
/*
 * Java business for entity table conversation 
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
import ci.orange.chatapi.utils.contract.IBasicBusiness;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.dto.transformer.*;
import ci.orange.chatapi.dao.entity.Conversation;
import ci.orange.chatapi.dao.entity.TypeConversation;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;
import org.springframework.transaction.annotation.Transactional;

/**
BUSINESS for table "conversation"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class ConversationBusiness implements IBasicBusiness<Request<ConversationDto>, Response<ConversationDto>> {

    private Response<ConversationDto> response;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationUserRepository conversationUserRepository;
    @Autowired
    private TypeConversationRepository typeConversationRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationUserBusiness conversationUserBusiness;
    @Autowired
    private MessageBusiness messageBusiness;
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

	public ConversationBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	/**
	 * create Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
    public Response<ConversationDto> create(Request<ConversationDto> request, Locale locale)  throws ParseException {
		log.info("----begin create Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();

            fieldsToVerify.put("typeConversationCode", dto.getTypeConversationCode());

			fieldsToVerify.put("participantIds", dto.getParticipantIds());

            // pour conversation privée le message est obligatoire.
            if (Utilities.areEquals(dto.getTypeConversationCode(), "PRIVATE")) {
                fieldsToVerify.put("firstMessage", dto.getFirstMessage());
            }

            // pour conversation de groupe la titre de la conversation est obligatoire
            if (Utilities.areEquals(dto.getTypeConversationCode(), "GROUP")) {
                fieldsToVerify.put("titre", dto.getTitre());
            }

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}


             /*
              contrainte Générales.
            */

            // Verify if typeConversationCode exist
            TypeConversation existingTypeConversation = null;
            existingTypeConversation = typeConversationRepository.findByCode(dto.getTypeConversationCode(), false);
            if (existingTypeConversation == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le Type de conversation invalide. Autorisé : PRIVATE, GROUP" + dto.getTypeConversationId(), locale));
                response.setHasError(true);
                return response;
            }

            // verifier si la liste des participants n'est pas vide
            if  (dto.getParticipantIds().isEmpty()) {
                response.setStatus(functionalError.FIELD_EMPTY("Cette liste ne dois pas être vide", locale));
                response.setHasError(true);
                return response;
            }

            // verifier que l'utilisateur existe bien.
            User actor = userRepository.findOne(request.getUser(), false);
            if (actor == null ) {
                response.setStatus(functionalError.DATA_NOT_EXIST(
                        "Ce utilisateur n'existe pas. UserId : " + request.getUser(), locale));
                response.setHasError(true);
                return response;
            }

            // verifier que le créateur de la conversation n'est pas inclus dans la liste des participants
            if (dto.getParticipantIds().contains(request.getUser())) {
                response.setStatus(functionalError.REQUEST_ERROR(
                        "Le créateur ne doit pas être inclus dans la liste des participants",
                        locale
                ));
                response.setHasError(true);
                return response;
            }

            // verifier que les participants existe bien
            for (Integer userId : dto.getParticipantIds()) {
                User user = userRepository.findOne(userId, false);
                if (user == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Ce participant n'existe pas." + userId, locale));
                    response.setHasError(true);
                    return response;
                }
            }


            /*
              contrainte pour la conversation privée.
            */

            if (Utilities.areEquals(dto.getTypeConversationCode(), "PRIVATE")) {

                // verifier que la liste de participant soit à 1. (en plus du créateur de la conversation.)
                if (dto.getParticipantIds().size() != 1) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Une conversation privée doit avoir exactement un participant." + dto.getParticipantIds(),
                            locale));
                    response.setHasError(true);
                    return response;
                }

                int participantId = dto.getParticipantIds().get(0);
                int actorId = actor.getId();

                // verifier si il existe deja une conversation entre les deux utilisateurs
                List<Conversation> existingConversations = conversationRepository
                        .findExistingPrivateConversation(actorId, participantId);

                if (existingConversations != null && !existingConversations.isEmpty()) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Une conversation privée existe déjà entre ces deux utilisateurs", locale));
                    response.setHasError(true);
                    return response;
                }


                //récupérer le nom du participant pour le mettre comme titre de la conversation
                User participant = userRepository.findOne(participantId, false);
                dto.setTitre(participant.getNom() + " " + participant.getPrenoms());

            }
//            else if (Utilities.areEquals(dto.getTypeConversationCode(), "GROUP")) {}


            // persistance de la conversation
            Conversation entityToSave = null;
			entityToSave = ConversationTransformer.INSTANCE.toEntity(dto, existingTypeConversation);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);

            // pour pourvoir utilisé l'id générer
            Conversation entitySaved = conversationRepository.save(entityToSave);

            log.info("----begin Ajout member in conversation -----");

            Request<ConversationUserDto> conversationUserDtoRequest = new Request<ConversationUserDto>();
            conversationUserDtoRequest.setUser(actor.getId());

            List<ConversationUserDto> liste = new ArrayList<ConversationUserDto>();

            // ajouter le créateur de la conversation dans la conversation
            ConversationUserDto creatorDto = new ConversationUserDto();
            creatorDto.setConversationId(entitySaved.getId());
            creatorDto.setUserId(actor.getId());
            creatorDto.setRole(true); // en tant qu'administrateur
            liste.add(creatorDto);

            // ajouter des participants de la conversation dans la conversation
            for (Integer participantId : dto.getParticipantIds()) {
                ConversationUserDto participantDto = new ConversationUserDto();
                participantDto.setConversationId(entitySaved.getId());
                participantDto.setUserId(participantId);
                participantDto.setRole(false); // en tant qu'administrateur
                liste.add(participantDto);
            }
            conversationUserDtoRequest.setDatas(liste);

            // le business d'ajout de membre se charge de la requête.
            Response<ConversationUserDto> conversationUserDtoResponse = conversationUserBusiness.create(
                    conversationUserDtoRequest, locale);
            if (conversationUserDtoResponse.isHasError()) {
                return response;
            }

            log.info("----end Ajout member in conversation -----");


            if (dto.getFirstMessage() != null ) {
                log.info("----Begin Ajout first message -----");

                Request<MessageDto> messageDtoRequest = new Request<MessageDto>();
                messageDtoRequest.setUser(actor.getId());

                List<MessageDto> messageDtoList = new ArrayList<MessageDto>();

                dto.getFirstMessage().setConversationId(entitySaved.getId());

                messageDtoList.add(dto.getFirstMessage());

                messageDtoRequest.setDatas(messageDtoList);

                // le business de création de message se charge de la requête
                Response<MessageDto> messageDtoResponse =  messageBusiness.create(messageDtoRequest, locale);

                if (messageDtoResponse.isHasError()) {
                    return response;
                }

                log.info("----end Ajout first message -----");

            }

            items.add(entitySaved);
		}

		if (!items.isEmpty()) {
			List<Conversation> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = conversationRepository.saveAll((Iterable<Conversation>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversation", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end create Conversation-----");
		return response;
	}

	/**
	 * update Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> update(Request<ConversationDto> request, Locale locale)  throws ParseException {
		log.info("----begin update Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversation existe
			Conversation entityToSave = null;
			entityToSave = conversationRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversation id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if typeConversation exist
			if (dto.getTypeConversationId() != null && dto.getTypeConversationId() > 0){
				TypeConversation existingTypeConversation = typeConversationRepository.findOne(dto.getTypeConversationId(), false);
				if (existingTypeConversation == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("typeConversation typeConversationId -> " + dto.getTypeConversationId(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setTypeConversation(existingTypeConversation);
			}
			if (Utilities.notBlank(dto.getTitre())) {
				entityToSave.setTitre(dto.getTitre());
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
			List<Conversation> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = conversationRepository.saveAll((Iterable<Conversation>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversation", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end update Conversation-----");
		return response;
	}

	/**
	 * delete Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> delete(Request<ConversationDto> request, Locale locale)  {
		log.info("----begin delete Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversation existe
			Conversation existingEntity = null;

			existingEntity = conversationRepository.findOne(dto.getId(), false);
			if (existingEntity == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversation -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// -----------------------------------------------------------------------
			// ----------- CHECK IF DATA IS USED
			// -----------------------------------------------------------------------

			// conversationUser
			List<ConversationUser> listOfConversationUser = conversationUserRepository.findByConversationId(existingEntity.getId(), false);
			if (listOfConversationUser != null && !listOfConversationUser.isEmpty()){
				response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfConversationUser.size() + ")", locale));
				response.setHasError(true);
				return response;
			}
			// message
			List<Message> listOfMessage = messageRepository.findByConversationId(existingEntity.getId(), false);
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
			conversationRepository.saveAll((Iterable<Conversation>) items);

			response.setHasError(false);
		}

		log.info("----end delete Conversation-----");
		return response;
	}

	/**
	 * get Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> getByCriteria(Request<ConversationDto> request, Locale locale)  throws Exception {
		log.info("----begin get Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation> items 			 = conversationRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(items) : ConversationTransformer.INSTANCE.toDtos(items);

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
			response.setCount(conversationRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("conversation", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get Conversation-----");
		return response;
	}

	/**
	 * get full ConversationDto by using Conversation as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ConversationDto getFullInfos(ConversationDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
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
