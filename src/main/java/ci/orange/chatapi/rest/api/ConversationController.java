

/*
 * Java controller for entity table conversation 
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
Controller for table "conversation"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/conversation")
public class ConversationController {

	@Autowired
    private ControllerFactory<ConversationDto> controllerFactory;
	@Autowired
	private ConversationBusiness conversationBusiness;


	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationDto> create(@RequestBody Request<ConversationDto> request) {
    	log.info("start method /conversation/create");
        Response<ConversationDto> response = controllerFactory.create(conversationBusiness, request, FunctionalityEnum.CREATE_CONVERSATION);
		log.info("end method /conversation/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationDto> update(@RequestBody Request<ConversationDto> request) {
    	log.info("start method /conversation/update");
        Response<ConversationDto> response = controllerFactory.update(conversationBusiness, request, FunctionalityEnum.UPDATE_CONVERSATION);
		log.info("end method /conversation/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationDto> delete(@RequestBody Request<ConversationDto> request) {
    	log.info("start method /conversation/delete");
        Response<ConversationDto> response = controllerFactory.delete(conversationBusiness, request, FunctionalityEnum.DELETE_CONVERSATION);
		log.info("end method /conversation/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ConversationDto> getByCriteria(@RequestBody Request<ConversationDto> request) {
    	log.info("start method /conversation/getByCriteria");
        Response<ConversationDto> response = controllerFactory.getByCriteria(conversationBusiness, request, FunctionalityEnum.VIEW_CONVERSATION);
		log.info("end method /conversation/getByCriteria");
        return response;
    }
}
