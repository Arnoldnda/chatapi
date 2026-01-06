

/*
 * Java controller for entity table type_message 
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
Controller for table "type_message"
 * 
 * @author SFL Back-End developper
 *
 */
@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/typeMessage")
public class TypeMessageController {

	@Autowired
    private ControllerFactory<TypeMessageDto> controllerFactory;
	@Autowired
	private TypeMessageBusiness typeMessageBusiness;

	@RequestMapping(value="/create",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeMessageDto> create(@RequestBody Request<TypeMessageDto> request) {
    	log.info("start method /typeMessage/create");
        Response<TypeMessageDto> response = controllerFactory.create(typeMessageBusiness, request, FunctionalityEnum.CREATE_TYPE_MESSAGE);
		log.info("end method /typeMessage/create");
        return response;
    }

	@RequestMapping(value="/update",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeMessageDto> update(@RequestBody Request<TypeMessageDto> request) {
    	log.info("start method /typeMessage/update");
        Response<TypeMessageDto> response = controllerFactory.update(typeMessageBusiness, request, FunctionalityEnum.UPDATE_TYPE_MESSAGE);
		log.info("end method /typeMessage/update");
        return response;
    }

	@RequestMapping(value="/delete",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeMessageDto> delete(@RequestBody Request<TypeMessageDto> request) {
    	log.info("start method /typeMessage/delete");
        Response<TypeMessageDto> response = controllerFactory.delete(typeMessageBusiness, request, FunctionalityEnum.DELETE_TYPE_MESSAGE);
		log.info("end method /typeMessage/delete");
        return response;
    }

	@RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<TypeMessageDto> getByCriteria(@RequestBody Request<TypeMessageDto> request) {
    	log.info("start method /typeMessage/getByCriteria");
        Response<TypeMessageDto> response = controllerFactory.getByCriteria(typeMessageBusiness, request, FunctionalityEnum.VIEW_TYPE_MESSAGE);
		log.info("end method /typeMessage/getByCriteria");
        return response;
    }
}
