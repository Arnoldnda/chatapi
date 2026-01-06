

/*
 * Java controller for entity table message 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2017 Savoir Faire Linux. All Rights Reserved.
 */

package ci.orange.chatapi.rest.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.enums.FunctionalityEnum;
import ci.orange.chatapi.business.*;
import ci.orange.chatapi.rest.fact.ControllerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

/**
Controller for table "message"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/message")
public class MessageController {

	@Autowired
    private ControllerFactory<MessageDto> controllerFactory;
	@Autowired
	private MessageBusiness messageBusiness;
    @Autowired
    private FIleStorageBusiness fIleStorageBusiness;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private ExceptionUtils       exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<MessageDto> create(@RequestBody Request<MessageDto> request) {
    	log.info("start method /message/create");
        Response<MessageDto> response = controllerFactory.create(messageBusiness, request, FunctionalityEnum.CREATE_MESSAGE);
		log.info("end method /message/create");
        return response;
    }

    @RequestMapping(value="/private/send",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<MessageDto> sendPrivateMessage(@RequestBody Request<MessageDto> request) {
        log.info("start method /message/private/send");

        Response<MessageDto> response   = new Response<MessageDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale        locale     = new Locale(languageID, "");

        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = messageBusiness.sendPrivateMessage(request, locale);
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

        log.info("end method /message/private/send");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<MessageDto> update(@RequestBody Request<MessageDto> request) {
    	log.info("start method /message/update");
        Response<MessageDto> response = controllerFactory.update(messageBusiness, request, FunctionalityEnum.UPDATE_MESSAGE);
		log.info("end method /message/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<MessageDto> delete(@RequestBody Request<MessageDto> request) {
    	log.info("start method /message/delete");
        Response<MessageDto> response = controllerFactory.delete(messageBusiness, request, FunctionalityEnum.DELETE_MESSAGE);
		log.info("end method /message/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<MessageDto> getByCriteria(@RequestBody Request<MessageDto> request) {
    	log.info("start method /message/getByCriteria");
        Response<MessageDto> response = controllerFactory.getByCriteria(messageBusiness, request, FunctionalityEnum.VIEW_MESSAGE);
		log.info("end method /message/getByCriteria");
        return response;
    }


    @RequestMapping(value = "/upload-image", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UploadFileDto> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {

        Response<UploadFileDto> response   = new Response<UploadFileDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale        locale     = new Locale(languageID, "");

        try {

            String url = fIleStorageBusiness.upload(file, locale);
            UploadFileDto dto = new UploadFileDto();
            dto.setUrl(url);
            response.setItems((List.of(dto)));
            response.setHasError(false);

        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }
        return response;
    }
}



