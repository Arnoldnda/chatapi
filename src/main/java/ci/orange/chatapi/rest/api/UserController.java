

/*
 * Java controller for entity table user 
 * Created on 2026-01-03 ( Time 10:00:04 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2017 Savoir Faire Linux. All Rights Reserved.
 */

package ci.orange.chatapi.rest.api;

import ci.orange.chatapi.dao.entity.User;
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
Controller for table "user"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/user")
public class UserController {

	@Autowired
    private ControllerFactory<UserDto> controllerFactory;
	@Autowired
	private UserBusiness userBusiness;
    @Autowired
    private FunctionalError      functionalError;
    @Autowired
    private ExceptionUtils       exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="/login",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UserDto> login(@RequestBody Request<UserDto> request) {
        log.info("start method /user/login");

        Response<UserDto> response   = new Response<UserDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = userBusiness.login(request, locale);
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
        log.info("end method /user/login");
        return response;
    }

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UserDto> create(@RequestBody Request<UserDto> request) {
    	log.info("start method /user/create");
        Response<UserDto> response = controllerFactory.create(userBusiness, request, FunctionalityEnum.CREATE_USER);
		log.info("end method /user/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UserDto> update(@RequestBody Request<UserDto> request) {
    	log.info("start method /user/update");
        Response<UserDto> response = controllerFactory.update(userBusiness, request, FunctionalityEnum.UPDATE_USER);
		log.info("end method /user/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UserDto> delete(@RequestBody Request<UserDto> request) {
    	log.info("start method /user/delete");
        Response<UserDto> response = controllerFactory.delete(userBusiness, request, FunctionalityEnum.DELETE_USER);
		log.info("end method /user/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UserDto> getByCriteria(@RequestBody Request<UserDto> request) {
    	log.info("start method /user/getByCriteria");
        Response<UserDto> response = controllerFactory.getByCriteria(userBusiness, request, FunctionalityEnum.VIEW_USER);
		log.info("end method /user/getByCriteria");
        return response;
    }
}
