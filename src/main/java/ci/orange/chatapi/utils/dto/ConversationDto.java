
/*
 * Java dto for entity table conversation 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.*;

import ci.orange.chatapi.utils.dto.base._ConversationDto;

/**
 * DTO for table "conversation"
 *
 * @author Geo
 */
@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class ConversationDto extends _ConversationDto{

    private String    statusLibelle               ;

    //Pour la liste des participants de la conversation
    private List<Integer> participantIds           ;

    // Pour le premier message de la conversation privé
    private MessageDto firstMessage                 ;
    
	//----------------------------------------------------------------------
    // clone METHOD
    //----------------------------------------------------------------------
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
