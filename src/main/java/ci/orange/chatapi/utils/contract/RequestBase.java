/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.contract;

import lombok.*;

/**
 * Request Base
 * 
 * @author Geo
 *
 */
@Data
@ToString
@NoArgsConstructor
public class RequestBase {
	protected String		sessionUser;
	protected Integer		size;
	protected Integer		index;
	protected String		lang;
	protected Boolean		isAnd;
	protected Integer		user;
	protected Boolean 		isSimpleLoading;
    protected String        action;
}