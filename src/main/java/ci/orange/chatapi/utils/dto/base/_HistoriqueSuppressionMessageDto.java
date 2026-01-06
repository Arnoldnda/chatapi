
/*
 * Java dto for entity table historique_suppression_message 
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
 * DTO customize for table "historique_suppression_message"
 * 
 * @author Smile Back-End generator
 *
 */
@Data
@ToString
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class _HistoriqueSuppressionMessageDto implements Cloneable {

    protected Integer    id                   ; // Primary Key

    protected Integer    messageId            ;
    protected Integer    userId               ;
    protected Boolean    isHidden             ;
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
	//protected Integer    message;

	// Search param
	protected SearchParam<Integer>  idParam               ;                     
	protected SearchParam<Integer>  messageIdParam        ;                     
	protected SearchParam<Integer>  userIdParam           ;                     
	protected SearchParam<Boolean>  isHiddenParam         ;                     
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
	protected SearchParam<Integer>  messageParam          ;                     

	// order param
	protected String orderField;
	protected String orderDirection;




}
