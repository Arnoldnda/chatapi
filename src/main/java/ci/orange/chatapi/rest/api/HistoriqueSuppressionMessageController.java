

/*
 * Java controller for entity table historique_suppression_message 
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
Controller for table "historique_suppression_message"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/historiqueSuppressionMessage")
public class HistoriqueSuppressionMessageController {

	@Autowired
    private ControllerFactory<HistoriqueSuppressionMessageDto> controllerFactory;
	@Autowired
	private HistoriqueSuppressionMessageBusiness historiqueSuppressionMessageBusiness;
    @Autowired
    private HttpServletRequest requestBasic;
    @Autowired
    private FunctionalError      functionalError;
    @Autowired
    private ExceptionUtils       exceptionUtils;

    @RequestMapping(value="/deleteMessage",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriqueSuppressionMessageDto> deleteMessageLocally(@RequestBody Request<HistoriqueSuppressionMessageDto> request) {
        log.info("start method historiqueSuppressionMessage/deleteMessage");
        Response<HistoriqueSuppressionMessageDto> response   = new Response<HistoriqueSuppressionMessageDto>();
        String        languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale     = new Locale(languageID, "");
        try {
            response = Validate.validateList(request, response, functionalError, locale);
            if (!response.isHasError()) {
                response = historiqueSuppressionMessageBusiness.deleteMessageLocally(request, locale);
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

        log.info("end method historiqueSuppressionMessage/deleteMessage");
        return response;
    }

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriqueSuppressionMessageDto> create(@RequestBody Request<HistoriqueSuppressionMessageDto> request) {
    	log.info("start method /historiqueSuppressionMessage/create");
        Response<HistoriqueSuppressionMessageDto> response = controllerFactory.create(historiqueSuppressionMessageBusiness, request, FunctionalityEnum.CREATE_HISTORIQUE_SUPPRESSION_MESSAGE);
		log.info("end method /historiqueSuppressionMessage/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriqueSuppressionMessageDto> update(@RequestBody Request<HistoriqueSuppressionMessageDto> request) {
    	log.info("start method /historiqueSuppressionMessage/update");
        Response<HistoriqueSuppressionMessageDto> response = controllerFactory.update(historiqueSuppressionMessageBusiness, request, FunctionalityEnum.UPDATE_HISTORIQUE_SUPPRESSION_MESSAGE);
		log.info("end method /historiqueSuppressionMessage/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriqueSuppressionMessageDto> delete(@RequestBody Request<HistoriqueSuppressionMessageDto> request) {
    	log.info("start method /historiqueSuppressionMessage/delete");
        Response<HistoriqueSuppressionMessageDto> response = controllerFactory.delete(historiqueSuppressionMessageBusiness, request, FunctionalityEnum.DELETE_HISTORIQUE_SUPPRESSION_MESSAGE);
		log.info("end method /historiqueSuppressionMessage/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriqueSuppressionMessageDto> getByCriteria(@RequestBody Request<HistoriqueSuppressionMessageDto> request) {
    	log.info("start method /historiqueSuppressionMessage/getByCriteria");
        Response<HistoriqueSuppressionMessageDto> response = controllerFactory.getByCriteria(historiqueSuppressionMessageBusiness, request, FunctionalityEnum.VIEW_HISTORIQUE_SUPPRESSION_MESSAGE);
		log.info("end method /historiqueSuppressionMessage/getByCriteria");
        return response;
    }
}
