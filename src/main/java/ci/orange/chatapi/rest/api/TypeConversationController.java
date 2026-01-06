

/*
 * Java controller for entity table type_conversation 
 * Created on 2026-01-03 ( Time 10:00:04 )
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
Controller for table "type_conversation"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/typeConversation")
public class TypeConversationController {

	@Autowired
    private ControllerFactory<TypeConversationDto> controllerFactory;
	@Autowired
	private TypeConversationBusiness typeConversationBusiness;

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeConversationDto> create(@RequestBody Request<TypeConversationDto> request) {
    	log.info("start method /typeConversation/create");
        Response<TypeConversationDto> response = controllerFactory.create(typeConversationBusiness, request, FunctionalityEnum.CREATE_TYPE_CONVERSATION);
		log.info("end method /typeConversation/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeConversationDto> update(@RequestBody Request<TypeConversationDto> request) {
    	log.info("start method /typeConversation/update");
        Response<TypeConversationDto> response = controllerFactory.update(typeConversationBusiness, request, FunctionalityEnum.UPDATE_TYPE_CONVERSATION);
		log.info("end method /typeConversation/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeConversationDto> delete(@RequestBody Request<TypeConversationDto> request) {
    	log.info("start method /typeConversation/delete");
        Response<TypeConversationDto> response = controllerFactory.delete(typeConversationBusiness, request, FunctionalityEnum.DELETE_TYPE_CONVERSATION);
		log.info("end method /typeConversation/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeConversationDto> getByCriteria(@RequestBody Request<TypeConversationDto> request) {
    	log.info("start method /typeConversation/getByCriteria");
        Response<TypeConversationDto> response = controllerFactory.getByCriteria(typeConversationBusiness, request, FunctionalityEnum.VIEW_TYPE_CONVERSATION);
		log.info("end method /typeConversation/getByCriteria");
        return response;
    }
}
