

/*
 * Java controller for entity table conversation_user 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2017 Savoir Faire Linux. All Rights Reserved.
 */

package ci.orange.chatapi.rest.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.enums.FunctionalityEnum;
import ci.orange.chatapi.business.*;
import ci.orange.chatapi.rest.fact.ControllerFactory;

import java.util.Locale;

/**
Controller for table "conversation_user"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/conversationUser")
public class ConversationUserController {

	@Autowired
    private ControllerFactory<ConversationUserDto> controllerFactory;
	@Autowired
	private ConversationUserBusiness conversationUserBusiness;
    @Autowired
    private HttpServletRequest requestBasic;
    @Autowired
    private FunctionalError      functionalError;
    @Autowired
    private ExceptionUtils       exceptionUtils;

    @RequestMapping(value="/group/add",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> addUserToGroup(@RequestBody Request<ConversationUserDto> request) {
        log.info("start method conversationUser/addUserToGroup");
        Response<ConversationUserDto> response   = new Response<ConversationUserDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = conversationUserBusiness.addUserToGroup(request, locale);
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            if (!response.isHasError()) {
                log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
            }
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }

        log.info("end method conversationUser/addUserToGroup");
        return response;
    }

    @RequestMapping(value="/group/remove",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> removeUserToGroup(@RequestBody Request<ConversationUserDto> request) {
        log.info("start method conversationUser/removeUserToGroup");
        Response<ConversationUserDto> response   = new Response<ConversationUserDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = conversationUserBusiness.removeUserToGroup(request, locale);
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            if (!response.isHasError()) {
                log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
            }
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }

        log.info("end method conversationUser/removeUserToGroup");
        return response;
    }

    @RequestMapping(value="/group/leave",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> leaveGroup(@RequestBody Request<ConversationUserDto> request) {
        log.info("start method conversationUser/leaveGroup");
        Response<ConversationUserDto> response   = new Response<ConversationUserDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = conversationUserBusiness.leaveGroup(request, locale);
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            if (!response.isHasError()) {
                log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
            }
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }

        log.info("end method conversationUser/leaveGroup");
        return response;
    }

    @RequestMapping(value="/deleteConversation",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> deleteConversationLocally(@RequestBody Request<ConversationUserDto> request) {
        log.info("start method conversationUser/deleteConversationLocally");
        Response<ConversationUserDto> response   = new Response<ConversationUserDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = conversationUserBusiness.deleteConversationLocally(request, locale);
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            if (!response.isHasError()) {
                log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
            } else {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
            }
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }

        log.info("end method conversationUser/deleteConversationLocally");
        return response;
    }

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> create(@RequestBody Request<ConversationUserDto> request) {
    	log.info("start method /conversationUser/create");
        Response<ConversationUserDto> response = controllerFactory.create(conversationUserBusiness, request, FunctionalityEnum.CREATE_CONVERSATION_USER);
		log.info("end method /conversationUser/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> update(@RequestBody Request<ConversationUserDto> request) {
    	log.info("start method /conversationUser/update");
        Response<ConversationUserDto> response = controllerFactory.update(conversationUserBusiness, request, FunctionalityEnum.UPDATE_CONVERSATION_USER);
		log.info("end method /conversationUser/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> delete(@RequestBody Request<ConversationUserDto> request) {
    	log.info("start method /conversationUser/delete");
        Response<ConversationUserDto> response = controllerFactory.delete(conversationUserBusiness, request, FunctionalityEnum.DELETE_CONVERSATION_USER);
		log.info("end method /conversationUser/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationUserDto> getByCriteria(@RequestBody Request<ConversationUserDto> request) {
    	log.info("start method /conversationUser/getByCriteria");
        Response<ConversationUserDto> response = controllerFactory.getByCriteria(conversationUserBusiness, request, FunctionalityEnum.VIEW_CONVERSATION_USER);
		log.info("end method /conversationUser/getByCriteria");
        return response;
    }
}
