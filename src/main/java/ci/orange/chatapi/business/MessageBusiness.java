                                                            													
/*
 * Java business for entity table message 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.business;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.IBasicBusiness;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.dto.transformer.*;
import ci.orange.chatapi.dao.entity.Message;
import ci.orange.chatapi.dao.entity.Conversation;
import ci.orange.chatapi.dao.entity.TypeMessage;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;
import org.springframework.transaction.annotation.Transactional;

/**
BUSINESS for table "message"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class MessageBusiness implements IBasicBusiness<Request<MessageDto>, Response<MessageDto>> {

	private Response<MessageDto> response;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private HistoriqueSuppressionMessageRepository historiqueSuppressionMessageRepository;
    @Autowired
    private ConversationUserRepository conversationUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
	private ConversationRepository conversationRepository;
    @Autowired
    private TypeConversationRepository typeConversationRepository ;
	@Autowired
	private TypeMessageRepository typeMessage2Repository;
	@Autowired
	private FunctionalError functionalError;
	@Autowired
	private TechnicalError technicalError;
    @Autowired
    private ParamsUtils paramsUtils;
    @Autowired
	private ExceptionUtils exceptionUtils;
	@PersistenceContext
	private EntityManager em;

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateTimeFormat;


	public MessageBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}


    /**
     * send private message by using MessageDto as object.
     *
     * @param request
     * @return response
     *
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<MessageDto> sendPrivateMessage(Request<MessageDto> request, Locale locale)  throws ParseException {
        Response<MessageDto> response = new Response<MessageDto>();
        try {
            log.info("----begin sendPrivateMessage-----");

            List<Message>        items    = new ArrayList<Message>();

            for (MessageDto dto : request.getDatas()) {
                // Definir les parametres obligatoires
                Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();

                fieldsToVerify.put("typeMessageCode", dto.getTypeMessageCode());
                fieldsToVerify.put("receiverId", dto.getReceiverId());

                // si le message est de type text le champ content est obligatoire
                if (Utilities.areEquals(dto.getTypeMessageCode(), "TEXT")) fieldsToVerify.put("content", dto.getContent());
                // si le message est de type image l'url de l'image est obligatoire
                if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE")) fieldsToVerify.put("imgUrl", dto.getImgUrl());
                // si le message est de type mixte les deux champs sont obligatoires
                if (Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {
                    fieldsToVerify.put("content", dto.getContent());
                    fieldsToVerify.put("imgUrl", dto.getImgUrl());
                }

                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }


                /*
                    Vérification metier
                */
                Integer actorId = request.getUser();
                Integer receiverId = dto.getReceiverId();

                // Verifier que l'utilisateur qui mène l'acton existe bien.
                // Verifier que l'utilisateur qui est censé recevoir le message
                User actor = userRepository.findOne(actorId, false);
                User receiver = userRepository.findOne(receiverId, false);
                if (actor == null || receiver == null ) {
                    response.setStatus(functionalError.DATA_NOT_EXIST(
                            "Ce utilisateur n'existe pas. UserId : " + request.getUser(), locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que celui qui envoi le message est différent de celui qui le reçoi
                if (Utilities.areEquals(actorId, receiverId)) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Impossible d'envoyer un message à soi-même. SenderId : " + request.getUser() + " ReceiverId : " + receiverId
                            , locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que le type de message est valide
                TypeMessage existingTypeMessage = typeMessage2Repository.findByCode(dto.getTypeMessageCode(), false);
                if (existingTypeMessage == null ) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Le Type de message invalide. Autorisé : TEXT, IMAGE, MIXED " + request.getUser(), locale));
                    response.setHasError(true);
                    return response;
                }

                // se rassurer de l'existance de l'image upload
                if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE") || Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {

                    // verifier que l'url viens de nous
                    if (!dto.getImgUrl().startsWith( paramsUtils.getBaseUrl())) {
                        response.setStatus(functionalError.REQUEST_ERROR( "Cette url n'est pas correct" ,  locale));
                        response.setHasError(true);
                        return response;
                    }

                    // verifier que le fichier existe vraiment
                    Path filePath = Paths.get(
                            paramsUtils.getBaseUploadPath(),
                            "messages",
                            Utilities.extractFileName(dto.getImgUrl())
                    );
                    if (!Files.exists(filePath)) {
                        response.setStatus(functionalError.REQUEST_FAIL( "Ce fichier n'existe pas." ,  locale));
                        response.setHasError(true);
                        return response;
                    }

                }

                //  verifier si il existe déja une conversation privé entre eux .
                // recupéré la conversaion
                Conversation existingConversation = null;
                Optional<Conversation> existingPrivateConversation = conversationRepository.findExistingPrivateConversation(actorId, receiverId);
                if (existingPrivateConversation.isEmpty()) {

                    log.info("Création d'une nouvelle conversation privée");

                    TypeConversation typePrivateConversation = typeConversationRepository.findByCode("PRIVATE", false);
                    if (typePrivateConversation == null) {
                        response.setStatus(functionalError.DATA_NOT_EXIST(
                                "Type PRIVATE non trouvé dans la base", locale));
                        response.setHasError(true);
                        return response;
                    }

                    // Créer la conversation
                    Conversation newConversation = new Conversation();
                    newConversation.setTitre("PRIVATE"); // géré dynamiquement côté front, par rapport à l'utilisateur connecté
                    newConversation.setTypeConversation(typePrivateConversation);
                    newConversation.setCreatedAt(Utilities.getCurrentDate());
                    newConversation.setCreatedBy(actorId);
                    newConversation.setIsDeleted(false);

                    existingConversation = conversationRepository.save(newConversation);

                    // Ajouter les deux participants
                    ConversationUser participant1 = new ConversationUser();
                    participant1.setConversation(existingConversation);
                    participant1.setUser(actor);
                    participant1.setHasLeft(false);
                    participant1.setHasDefinitivelyLeft(false);
                    participant1.setHasCleaned(false);
                    participant1.setIsDeleted(false);
                    participant1.setRole(false);
                    participant1.setCreatedAt(Utilities.getCurrentDate());
                    participant1.setCreatedBy(actorId);

                    ConversationUser participant2 = new ConversationUser();
                    participant2.setConversation(existingConversation);
                    participant2.setUser(receiver);
                    participant2.setHasLeft(false);
                    participant2.setHasDefinitivelyLeft(false);
                    participant2.setHasCleaned(false);
                    participant2.setIsDeleted(false);
                    participant2.setRole(false);
                    participant2.setCreatedAt(Utilities.getCurrentDate());
                    participant2.setCreatedBy(actorId);

                    conversationUserRepository.saveAll(Arrays.asList(participant1, participant2));

                    log.info("Conversation privée créée avec succès: " +  existingConversation.getId());

                } else {
                        existingConversation = existingPrivateConversation.get() ;
                }

                // assigné l'id de la conversation dans le message
                dto.setConversationId(existingConversation.getId());

                Message entityToSave = null;
                entityToSave = MessageTransformer.INSTANCE.toEntity(dto, existingConversation, existingTypeMessage);
                entityToSave.setCreatedAt(Utilities.getCurrentDate());
                entityToSave.setCreatedBy(request.getUser());
                entityToSave.setIsDeleted(false);
                items.add(entityToSave);
            }

            if (!items.isEmpty()) {
                List<Message> itemsSaved = null;
                // inserer les donnees en base de donnees
                itemsSaved = messageRepository.saveAll((Iterable<Message>) items);
                if (itemsSaved == null) {
                    response.setStatus(functionalError.SAVE_FAIL("message", locale));
                    response.setHasError(true);
                    return response;
                }
                List<MessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? MessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : MessageTransformer.INSTANCE.toDtos(itemsSaved);

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

            log.info("----end sendPrivateMessage-----");
            return response;

        } catch (PermissionDeniedDataAccessException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (DataAccessResourceFailureException e) {
            exceptionUtils.DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(response, locale, e);
        } catch (DataAccessException e) {
            exceptionUtils.DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        } finally {
            if (response.isHasError() && response.getStatus() != null) {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                throw new RuntimeException(response.getStatus().getCode() + ";" + response.getStatus().getMessage());
            }
        }
        return response;
    }


    /**
     * send group message by using MessageDto as object.
     *
     * @param request
     * @return response
     *
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<MessageDto> sendGroupMessage(Request<MessageDto> request, Locale locale)  throws ParseException {
        Response<MessageDto> response = new Response<MessageDto>();
        try {
            log.info("----begin sendGroupMessage-----");
            List<Message>        items    = new ArrayList<Message>();

            for (MessageDto dto : request.getDatas()) {
                // Definir les parametres obligatoires
                Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();

                fieldsToVerify.put("typeMessageCode", dto.getTypeMessageCode());
                fieldsToVerify.put("conversationId", dto.getConversationId());

                // si le message est de type text le champ content est obligatoire
                if (Utilities.areEquals(dto.getTypeMessageCode(), "TEXT")) fieldsToVerify.put("content", dto.getContent());
                // si le message est de type image l'url de l'image est obligatoire
                if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE")) fieldsToVerify.put("imgUrl", dto.getImgUrl());
                // si le message est de type mixte les deux champs sont obligatoires
                if (Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {
                    fieldsToVerify.put("content", dto.getContent());
                    fieldsToVerify.put("imgUrl", dto.getImgUrl());
                }

                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }

                Integer actorId = request.getUser();

                // verifier que l'utilisateur existe bien.
                User actor = userRepository.findOne(actorId, false);
                if (actor == null ) {
                    response.setStatus(functionalError.DATA_NOT_EXIST(
                            "Ce utilisateur n'existe pas. UserId : " + request.getUser(), locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que le type de message est valide
                TypeMessage existingTypeMessage = typeMessage2Repository.findByCode(dto.getTypeMessageCode(), false);
                if (existingTypeMessage == null ) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Le Type de message invalide. Autorisé : TEXT, IMAGE, MIXED " + request.getUser(), locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que la conversation existe
                Conversation existingConversation = conversationRepository.findOne(dto.getConversationId(),false);
                if (existingConversation == null ) {
                    response.setStatus(functionalError.DATA_NOT_EXIST(
                            "Cette conversation n'existe pas. ConversationId : " + dto.getConversationId(),
                            locale)
                    );
                    response.setHasError(true);
                    return response;
                }

                // verifier que la conversation est de type groupe
                if (Utilities.areNotEquals(existingConversation.getTypeConversation().getCode(), "GROUP")) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Cette conversation n'est pas de type GROUP", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que l'utilisateur existe dans la conversation
                ConversationUser conversationUser = conversationUserRepository.findActiveUserInConversation(
                        dto.getConversationId(), request.getUser());
                if (conversationUser == null ) {
                    response.setStatus(functionalError.UNAUTHORIZED(
                            "Vous n'êtes pas membre de cette conversation. ConversationId : " + dto.getConversationId(),
                            locale)
                    );
                    response.setHasError(true);
                    return response;
                }

                // Vérifier l'existence de l'image si nécessaire
                if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE") || Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {

                    // verifier que l'url viens de nous
                    if (!dto.getImgUrl().startsWith( paramsUtils.getBaseUrl())) {
                        response.setStatus(functionalError.REQUEST_ERROR( "Cette url n'est pas correct" ,  locale));
                        response.setHasError(true);
                        return response;
                    }

                    // verifier que le fichier existe vraiment
                    Path filePath = Paths.get(
                            paramsUtils.getBaseUploadPath(),
                            "messages",
                            Utilities.extractFileName(dto.getImgUrl())
                    );
                    if (!Files.exists(filePath)) {
                        response.setStatus(functionalError.REQUEST_FAIL( "Ce fichier n'existe pas." ,  locale));
                        response.setHasError(true);
                        return response;
                    }

                }

                Message entityToSave = null;
                entityToSave = MessageTransformer.INSTANCE.toEntity(dto, existingConversation, existingTypeMessage);
                entityToSave.setCreatedAt(Utilities.getCurrentDate());
                entityToSave.setCreatedBy(request.getUser());
                entityToSave.setIsDeleted(false);
                items.add(entityToSave);
            }

            if (!items.isEmpty()) {
                List<Message> itemsSaved = null;
                // inserer les donnees en base de donnees
                itemsSaved = messageRepository.saveAll((Iterable<Message>) items);
                if (itemsSaved == null) {
                    response.setStatus(functionalError.SAVE_FAIL("message", locale));
                    response.setHasError(true);
                    return response;
                }
                List<MessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? MessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : MessageTransformer.INSTANCE.toDtos(itemsSaved);

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

            log.info("----end sendGroupMessage-----");
            return response;

        } catch (PermissionDeniedDataAccessException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (DataAccessResourceFailureException e) {
            exceptionUtils.DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(response, locale, e);
        } catch (DataAccessException e) {
            exceptionUtils.DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        } finally {
            if (response.isHasError() && response.getStatus() != null) {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                throw new RuntimeException(response.getStatus().getCode() + ";" + response.getStatus().getMessage());
            }
        }

        return response;
    }

	/**
	 * create Message by using MessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<MessageDto> create(Request<MessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin create Message-----");

		Response<MessageDto> response = new Response<MessageDto>();
		List<Message>        items    = new ArrayList<Message>();

			
		for (MessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();

            fieldsToVerify.put("typeMessageCode", dto.getTypeMessageCode());
            fieldsToVerify.put("conversationId", dto.getConversationId());

            // si le message est de type text le champ content est obligatoire
            if (Utilities.areEquals(dto.getTypeMessageCode(), "TEXT")) fieldsToVerify.put("content", dto.getContent());
            // si le message est de type image l'url de l'image est obligatoire
            if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE")) fieldsToVerify.put("imgUrl", dto.getImgUrl());
            // si le message est de type mixte les deux champs sont obligatoires
            if (Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {
                fieldsToVerify.put("content", dto.getContent());
                fieldsToVerify.put("imgUrl", dto.getImgUrl());
            }

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

            if (Utilities.areEquals(dto.getTypeMessageCode(), "IMAGE") || Utilities.areEquals(dto.getTypeMessageCode(), "MIXED")) {

                // verifier que l'url viens de nous
                if (!dto.getImgUrl().startsWith( paramsUtils.getBaseUrl())) {
                    response.setStatus(functionalError.REQUEST_ERROR( "Cette url n'est pas correct" ,  locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que le fichier existe vraiment
                Path filePath = Paths.get(
                        paramsUtils.getBaseUploadPath(),
                        "messages",
                        Utilities.extractFileName(dto.getImgUrl())
                );
                if (!Files.exists(filePath)) {
                    response.setStatus(functionalError.REQUEST_FAIL( "Ce fichier n'existe pas." ,  locale));
                    response.setHasError(true);
                    return response;
                }

            }

            // verifier que le type de message est valide
            TypeMessage existingTypeMessage = typeMessage2Repository.findByCode(dto.getTypeMessageCode(), false);
            if (existingTypeMessage == null ) {
                response.setStatus(functionalError.REQUEST_ERROR(
                        "Le Type de message invalide. Autorisé : TEXT, IMAGE, MIXED " + request.getUser(), locale));
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

            // verifier que la conversation existe
            Conversation existingConversation = conversationRepository.findOne(dto.getConversationId(),false);
            if (existingConversation == null ) {
                response.setStatus(functionalError.DATA_NOT_EXIST(
                        "Cette conversation n'existe pas. ConversationId : " + dto.getConversationId(),
                        locale)
                );
                response.setHasError(true);
                return response;
            }

            // verifier que l'utilisateur existe dans la conversation
            ConversationUser conversationUser = conversationUserRepository.findActiveUserInConversation(
                    dto.getConversationId(), request.getUser());
            if (conversationUser == null ) {
                response.setStatus(functionalError.UNAUTHORIZED(
                        "L'utilisateur ne fait pas partir de cette conversation. ConversationId : " + dto.getConversationId(),
                        locale)
                );
                response.setHasError(true);
                return response;
            }

            Message entityToSave = null;
			entityToSave = MessageTransformer.INSTANCE.toEntity(dto, existingConversation, existingTypeMessage);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<Message> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = messageRepository.saveAll((Iterable<Message>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("message", locale));
				response.setHasError(true);
				return response;
			}
			List<MessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? MessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : MessageTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end create Message-----");
		return response;
	}


	/**
	 * update Message by using MessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<MessageDto> update(Request<MessageDto> request, Locale locale)  throws ParseException {
		log.info("----begin update Message-----");

		Response<MessageDto> response = new Response<MessageDto>();
		List<Message>        items    = new ArrayList<Message>();
			
		for (MessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la message existe
			Message entityToSave = null;
			entityToSave = messageRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("message id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
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
			// Verify if typeMessage2 exist
			if (dto.getTypeMessage() != null && dto.getTypeMessage() > 0){
				TypeMessage existingTypeMessage2 = typeMessage2Repository.findOne(dto.getTypeMessage(), false);
				if (existingTypeMessage2 == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("typeMessage2 typeMessage -> " + dto.getTypeMessage(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setTypeMessage2(existingTypeMessage2);
			}
			if (Utilities.notBlank(dto.getContent())) {
				entityToSave.setContent(dto.getContent());
			}
			if (Utilities.notBlank(dto.getImgUrl())) {
				entityToSave.setImgUrl(dto.getImgUrl());
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
			List<Message> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = messageRepository.saveAll((Iterable<Message>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("message", locale));
				response.setHasError(true);
				return response;
			}
			List<MessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? MessageTransformer.INSTANCE.toLiteDtos(itemsSaved) : MessageTransformer.INSTANCE.toDtos(itemsSaved);

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

		log.info("----end update Message-----");
		return response;
	}

	/**
	 * delete Message by using MessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<MessageDto> delete(Request<MessageDto> request, Locale locale)  {
		log.info("----begin delete Message-----");

		Response<MessageDto> response = new Response<MessageDto>();
		List<Message>        items    = new ArrayList<Message>();
			
		for (MessageDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
            fieldsToVerify.put("conversationId", dto.getConversationId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
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

            // Verifier si la message existe
            Message existingEntity = null;
            existingEntity = messageRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Ce message n'existe pas. " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }

            // verifier que l'utilisateur existe dans la conversation
            ConversationUser conversationUser = conversationUserRepository.findActiveUserInConversation(
                    dto.getConversationId(), request.getUser());
            if (conversationUser == null ) {
                response.setStatus(functionalError.UNAUTHORIZED(
                        "L'utilisateur ne fait pas partir de cette conversation. ConversationId : " + dto.getConversationId(),
                        locale)
                );
                response.setHasError(true);
                return response;
            }

            // le message appartient à la conversation
            if (Utilities.areNotEquals(dto.getConversationId(), existingEntity.getConversation().getId())) {
                response.setStatus(functionalError.UNAUTHORIZED(
                        "Ce message n'est pas enregister dans cette conversation. ConversationId : " + dto.getConversationId(),
                        locale)
                );
                response.setHasError(true);
                return response;
            }

			// -----------------------------------------------------------------------
			// ----------- CHECK IF DATA IS USED
			// -----------------------------------------------------------------------

			// historiqueSuppressionMessage
			List<HistoriqueSuppressionMessage> listOfHistoriqueSuppressionMessage = historiqueSuppressionMessageRepository.findByMessageId(existingEntity.getId(), false);
			if (listOfHistoriqueSuppressionMessage != null && !listOfHistoriqueSuppressionMessage.isEmpty()){
				response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfHistoriqueSuppressionMessage.size() + ")", locale));
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
			messageRepository.saveAll((Iterable<Message>) items);

			response.setHasError(false);
		}

		log.info("----end delete Message-----");
		return response;
	}

	/**
	 * get Message by using MessageDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<MessageDto> getByCriteria(Request<MessageDto> request, Locale locale)  throws Exception {
		log.info("----begin get Message-----");

		Response<MessageDto> response = new Response<MessageDto>();
		List<Message> items 			 = messageRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<MessageDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? MessageTransformer.INSTANCE.toLiteDtos(items) : MessageTransformer.INSTANCE.toDtos(items);

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
			response.setCount(messageRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("message", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get Message-----");
		return response;
	}

	/**
	 * get full MessageDto by using Message as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private MessageDto getFullInfos(MessageDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
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
