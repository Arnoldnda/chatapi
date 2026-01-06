
/*
 * Java dto for entity table conversation_user 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.dto;

import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.*;

import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.dto.base._ConversationUserDto;

/**
 * DTO for table "conversation_user"
 *
 * @author Geo
 */
@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class ConversationUserDto extends _ConversationUserDto{

    private String    statusLibelle               ;
    
	//----------------------------------------------------------------------
    // clone METHOD
    //----------------------------------------------------------------------
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
