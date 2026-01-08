                                                                                        																				
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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
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
import ci.orange.chatapi.dao.entity.ConversationUser;
import ci.orange.chatapi.dao.entity.User;
import ci.orange.chatapi.dao.entity.Conversation;
import ci.orange.chatapi.dao.repository.*;
import org.springframework.transaction.annotation.Transactional;

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
     * add user in conversation by using ConversationUserDto as object.
     *
     * @param request
     * @return response
     *
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ConversationUserDto> addUserToGroup(Request<ConversationUserDto> request, Locale locale)  throws ParseException {
        Response<ConversationUserDto> response = new Response<ConversationUserDto>();
        try {
            log.info("----begin create ConversationUser-----");
            List<ConversationUser>        items    = new ArrayList<ConversationUser>();

            for (ConversationUserDto dto : request.getDatas()) {
                // Definir les parametres obligatoires
                Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
                fieldsToVerify.put("conversationId", dto.getConversationId());
                fieldsToVerify.put("userId", dto.getUserId());

                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }

                Integer actorId  = request.getUser();

              //  vérifier que l'acteur existe
                User actor = userRepository.findOne(actorId, false);
                if (actor == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur inexistant : " + actorId, locale));
                    response.setHasError(true);
                    return response;
                }

                // Verify if conversation exist
                Conversation existingConversation = null;
                existingConversation = conversationRepository.findOne(dto.getConversationId(), false);
                if (existingConversation == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Cette conversation n'existe pas." + dto.getConversationId(), locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que la conversation est un group
                if (Utilities.areNotEquals(existingConversation.getTypeConversation().getCode(), "GROUP")) {
                    response.setStatus(functionalError.REQUEST_ERROR("Ajout de membre autorisé uniquement pour les groupes", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier que l'acteur est un membre actif de la conversation
                ConversationUser actorMembership = conversationUserRepository.findActiveUserInConversation(
                        existingConversation.getId(), actorId);
                if (actorMembership == null) {
                    response.setStatus(functionalError.UNAUTHORIZED("Vous n'êtes pas membre actif de ce groupe", locale));
                    response.setHasError(true);
                    return response;
                }

                //  verifier que l'acteur est un admin
                if (!Utilities.isTrue(actorMembership.getRole())) {
                    response.setStatus(functionalError.UNAUTHORIZED("Seuls les administrateurs peuvent ajouter des membres", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier qu'on ne s'ajoute pas soit même
                if (Utilities.areEquals(actorId, dto.getUserId())) {
                    response.setStatus(functionalError.REQUEST_ERROR("Impossible de s'ajouter soi-même", locale));
                    response.setHasError(true);
                    return response;
                }

                // Verify if user exist
                User existingUser = null;
                existingUser = userRepository.findOne(dto.getUserId(), false);
                if (existingUser == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur à ajouté inexistant." + dto.getUserId(), locale));
                    response.setHasError(true);
                    return response;
                }

                // Historique du participant
                ConversationUser existing = conversationUserRepository.findByConversation_IdAndUser_IdAndIsDeletedFalse(
                        existingConversation.getId(), existingUser.getId());

                // verifier si le user à déja quitté la conversation pour faire une réintégration
                if (existing != null ) {
                    // verifier s'il a pas quitté le groupe
                    if (!Utilities.isTrue(existing.getHasLeft())) {
                        response.setStatus(functionalError.REQUEST_ERROR("Utilisateur déjà membre du groupe", locale));
                        response.setHasError(true);
                        return response;
                    }

                    // verifier s'il a définitivement quitté le groupe
                    if (Utilities.isTrue(existing.getHasDefinitivelyLeft())) {
                        response.setStatus(functionalError.REQUEST_ERROR(
                                "Cet utilisateur a quitté définitivement le groupe", locale));
                        response.setHasError(true);
                        return response;
                    }

                    // Réintégration
                    existing.setHasLeft(false);
                    existing.setRecreatedAt(Utilities.getCurrentDate());
                    existing.setRecreatedBy(actorId);
                    existing.setUpdatedAt(Utilities.getCurrentDate());
                    existing.setUpdatedBy(actorId);
                    existing.setIsDeleted(false);
                    items.add(existing);

                } else {
                    //Nouveau membre
                    ConversationUser entityToSave = null;
                    entityToSave = ConversationUserTransformer.INSTANCE.toEntity(dto, existingUser, existingConversation);
                    entityToSave.setRole(false);
                    entityToSave.setCreatedAt(Utilities.getCurrentDate());
                    entityToSave.setCreatedBy(request.getUser());
                    entityToSave.setIsDeleted(false);
                    entityToSave.setHasLeft(false);
                    entityToSave.setHasDefinitivelyLeft(false);
                    entityToSave.setHasCleaned(false);
                    items.add(entityToSave);
                }
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
     * remove user in conversation by using ConversationUserDto as object.
     *
     * @param request
     * @return response
     *
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ConversationUserDto> removeUserToGroup(Request<ConversationUserDto> request, Locale locale)  throws ParseException {
        Response<ConversationUserDto> response = new Response<ConversationUserDto>();
        try {
            log.info("----begin update ConversationUser-----");

            List<ConversationUser>        items    = new ArrayList<ConversationUser>();

            for (ConversationUserDto dto : request.getDatas()) {
                // Definir les parametres obligatoires
                Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
                fieldsToVerify.put("conversationId", dto.getConversationId());
                fieldsToVerify.put("userId", dto.getUserId());

                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }

                Integer actorId = request.getUser();

                // verifier si l'acteur existe
                User actor = userRepository.findOne(actorId, false);
                if (actor == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur inexistant : " + actorId, locale));
                    response.setHasError(true);
                    return response;
                }

                // Verify if conversation exist
                Conversation existingConversation = conversationRepository.findOne(dto.getConversationId(), false);
                if (existingConversation == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Conversation inexistante ou supprimée" + dto.getConversationId(), locale));
                    response.setHasError(true);
                    return response;
                }

                 // vérifié que c'est bien un groupe
                if (!Utilities.areEquals(existingConversation.getTypeConversation().getCode(), "GROUP")) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                                    "Retrait de membre autorisé uniquement pour les groupes", locale));
                    response.setHasError(true);
                    return response;
                }

                // vérifier si l'acteur est un membre actif de la conversation
                ConversationUser actorMembership = conversationUserRepository.findActiveUserInConversation(
                                existingConversation.getId(), actorId);
                if (actorMembership == null) {
                    response.setStatus( functionalError.UNAUTHORIZED(
                                    "Vous n'êtes pas membre actif de ce groupe", locale));
                    response.setHasError(true);
                    return response;
                }

                //vérifié que l'actor est admin
                if (!Utilities.isTrue(actorMembership.getRole())) {
                    response.setStatus(functionalError.UNAUTHORIZED(
                                    "Seuls les administrateurs peuvent retirer des membres", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier si l'utilisateur existe
                User userToRemove = userRepository.findOne(dto.getUserId(), false);
                if (userToRemove == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Utilisateur à retirer inexistant", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier si l'acteur est different de celui qu'on retire
                if (Utilities.areEquals(actorId, userToRemove.getId())) {
                    response.setStatus(functionalError.REQUEST_ERROR(
                            "Impossible de se retirer soi-même via cette action", locale));
                    response.setHasError(true);
                    return response;
                }

                //verifier si l'utilisateur est membre actif du groupe
                ConversationUser entityToSave = conversationUserRepository.findActiveUserInConversation(
                                existingConversation.getId(), userToRemove.getId());
                if (entityToSave == null) {
                    response.setStatus(functionalError.REQUEST_ERROR("Cet utilisateur n'est pas membre actif du groupe", locale));
                    response.setHasError(true);
                    return response;
                }

                // verifier si l'utisateur n'a pas déja été retirer ou n'est pas déja quitter
                if (entityToSave.getRecreatedAt() != null ) {
                    entityToSave.setHasDefinitivelyLeft(true);
                    entityToSave.setDefinitivelyLeftAt(Utilities.getCurrentDate());
                    entityToSave.setDefinitivelyLeftBy(actorId);
                }

                entityToSave.setHasLeft(true);
                entityToSave.setLeftAt(Utilities.getCurrentDate());
                entityToSave.setLeftBy(actorId);
                entityToSave.setUpdatedAt(Utilities.getCurrentDate());
                entityToSave.setUpdatedBy(actorId);
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

            log.info("----begin update ConversationUser-----");
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

			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

            // Verify if conversation exist
            Conversation existingConversation = null;
            existingConversation = conversationRepository.findOne(dto.getConversationId(), false);
            if (existingConversation == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Cette conversation n'existe pas." + dto.getConversationId(), locale));
                response.setHasError(true);
                return response;
            }

            // verifier si elle est prive
            if (Utilities.areEquals(existingConversation.getTypeConversation().getCode(), "PRIVATE")) {
                response.setStatus(functionalError.UNAUTHORIZED("Impossible d'ajouter un menbre. Cette conversation est privé ", locale));
                response.setHasError(true);
                return response;
            }

			// Verify if user exist
			User existingUser = null;
            existingUser = userRepository.findOne(dto.getUserId(), false);
            if (existingUser == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Ce utilisateur n'existe pas." + dto.getUserId(), locale));
                response.setHasError(true);
                return response;
            }


            // Vérifier que l'acteur est membre de la conversation
            ConversationUser conversationMember = conversationUserRepository.findByConversation_IdAndUser_IdAndIsDeletedFalse(
                    dto.getConversationId(), request.getUser()
            );
            if (conversationMember == null) {
                response.setStatus(functionalError.DISALLOWED_OPERATION(
                        "L'acteur n'est pas membre de la conversation", locale));
                response.setHasError(true);
                return response;
            }

            // verifier que l'acteur est admin
            if(!conversationMember.getRole()) {
                response.setStatus(functionalError.DISALLOWED_OPERATION(
                        "seul un administrateur peut ajouter un membre", locale));
                response.setHasError(true);
                return response;
            }

            // verifier si l'utlisateur existe déja dans cette conversation
            ConversationUser conversationMemberUser = conversationUserRepository.findByConversation_IdAndUser_IdAndIsDeletedFalse(
                    dto.getConversationId(), dto.getUserId()
            );
            if (conversationMemberUser != null) {
                response.setStatus(functionalError.DISALLOWED_OPERATION(
                        "Ce utilisateur existe déja dans le conversation", locale));
                response.setHasError(true);
                return response;
            }


            ConversationUser entityToSave = null;
			entityToSave = ConversationUserTransformer.INSTANCE.toEntity(dto, existingUser, existingConversation);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);
            entityToSave.setHasLeft(false);
            entityToSave.setHasDefinitivelyLeft(false);
            entityToSave.setHasCleaned(false);
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
