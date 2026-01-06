

/*
 * Java controller for entity table historique_suppression_message 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2017 Savoir Faire Linux. All Rights Reserved.
 */

package ci.orange.chatapi.rest.api;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
