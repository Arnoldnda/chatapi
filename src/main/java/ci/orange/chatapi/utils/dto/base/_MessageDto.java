
/*
 * Java dto for entity table message 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.dto.base;

import java.util.Date;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import lombok.*;

import ci.orange.chatapi.utils.contract.*;

/**
 * DTO customize for table "message"
 * 
 * @author Smile Back-End generator
 *
 */
@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class _MessageDto implements Cloneable {

    protected Integer    id                   ; // Primary Key

    protected String     content              ;
    protected String     imgUrl               ;
    protected Integer    typeMessage          ;
    protected Integer    conversationId       ;
	protected String     createdAt            ;
	protected String     updatedAt            ;
	protected String     deletedAt            ;
    protected Integer    createdBy            ;
    protected Integer    updatedBy            ;
    protected Integer    deletedBy            ;
    protected Boolean    isDeleted            ;

    //----------------------------------------------------------------------
    // ENTITY LINKS FIELD ( RELATIONSHIP )
    //----------------------------------------------------------------------
	//protected Integer    conversation;
	//protected Integer    typeMessage;
	protected String typeMessageLibelle;
	protected String typeMessageCode;

	// Search param
	protected SearchParam<Integer>  idParam               ;                     
	protected SearchParam<String>   contentParam          ;                     
	protected SearchParam<String>   imgUrlParam           ;                     
	protected SearchParam<Integer>  typeMessageParam      ;                     
	protected SearchParam<Integer>  conversationIdParam   ;                     
	protected SearchParam<String>   createdAtParam        ;                     
	protected SearchParam<String>   updatedAtParam        ;                     
	protected SearchParam<String>   deletedAtParam        ;                     
	protected SearchParam<Integer>  createdByParam        ;                     
	protected SearchParam<Integer>  updatedByParam        ;                     
	protected SearchParam<Integer>  deletedByParam        ;                     
	protected SearchParam<Boolean>  isDeletedParam        ;                     
	protected SearchParam<Integer>  conversationParam     ;                     
	protected SearchParam<String>   typeMessageLibelleParam;
	protected SearchParam<String>   typeMessageCodeParam  ;                     

	// order param
	protected String orderField;
	protected String orderDirection;




}
