
/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.contract;

import ci.orange.chatapi.utils.enums.*;

/**
 * IController
 * 
 * @author Geo
 *
 */
public interface IController<DTO> {
    Response<DTO> create(IBasicBusiness iBasicBusiness, Request<DTO> request, FunctionalityEnum functionalityEnum);

    Response<DTO> update(IBasicBusiness iBasicBusiness, Request<DTO> request, FunctionalityEnum functionalityEnum);

    Response<DTO> delete(IBasicBusiness iBasicBusiness, Request<DTO> request, FunctionalityEnum functionalityEnum);

    Response<DTO> getByCriteria(IBasicBusiness iBasicBusiness, Request<DTO> request, FunctionalityEnum functionalityEnum);
}

