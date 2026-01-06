
/*
 * Java dto for entity table conversation_user 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.dto.base;

import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import lombok.*;

import ci.orange.chatapi.utils.contract.*;

/**
 * DTO customize for table "conversation_user"
 * 
 * @author Smile Back-End generator
 *
 */
@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class _ConversationUserDto implements Cloneable {

    protected Integer    id                   ; // Primary Key

    protected Integer    conversationId       ;
    protected Integer    userId               ;
    protected Boolean    role                 ;
    protected Boolean    hasLeft              ;
	protected String     leftAt               ;
    protected Integer    leftBy               ;
    protected Boolean    hasDefinitivelyLeft  ;
	protected String     definitivelyLeftAt   ;
    protected Integer    definitivelyLeftBy   ;
	protected String     recreatedAt          ;
    protected Integer    recreatedBy          ;
    protected Boolean    hasCleaned           ;
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
	//protected Integer    user;
	protected String userNom;
	protected String userPrenoms;
	protected String userLogin;
	//protected Integer    conversation;

	// Search param
	protected SearchParam<Integer>  idParam               ;                     
	protected SearchParam<Integer>  conversationIdParam   ;                     
	protected SearchParam<Integer>  userIdParam           ;                     
	protected SearchParam<Boolean>  hasLeftParam          ;                     
	protected SearchParam<String>   leftAtParam           ;                     
	protected SearchParam<Integer>  leftByParam           ;                     
	protected SearchParam<Boolean>  hasDefinitivelyLeftParam;                     
	protected SearchParam<String>   definitivelyLeftAtParam;                     
	protected SearchParam<Integer>  definitivelyLeftByParam;                     
	protected SearchParam<String>   recreatedAtParam      ;                     
	protected SearchParam<Integer>  recreatedByParam      ;                     
	protected SearchParam<Boolean>  hasCleanedParam       ;                     
	protected SearchParam<String>   createdAtParam        ;                     
	protected SearchParam<String>   updatedAtParam        ;                     
	protected SearchParam<String>   deletedAtParam        ;                     
	protected SearchParam<Integer>  createdByParam        ;                     
	protected SearchParam<Integer>  updatedByParam        ;                     
	protected SearchParam<Integer>  deletedByParam        ;                     
	protected SearchParam<Boolean>  isDeletedParam        ;                     
	protected SearchParam<Integer>  userParam             ;                     
	protected SearchParam<String>   userNomParam          ;                     
	protected SearchParam<String>   userPrenomsParam      ;                     
	protected SearchParam<String>   userLoginParam        ;                     
	protected SearchParam<Integer>  conversationParam     ;                     

	// order param
	protected String orderField;
	protected String orderDirection;




}
