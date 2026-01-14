

/*
 * Java controller for entity table conversation 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2017 Savoir Faire Linux. All Rights Reserved.
 */

package ci.orange.chatapi.rest.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.enums.FunctionalityEnum;
import ci.orange.chatapi.business.*;
import ci.orange.chatapi.rest.fact.ControllerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    @Autowired
    private FunctionalError      functionalError;
    @Autowired
    private ExceptionUtils       exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;
    @Autowired
    private ParamsUtils paramsUtils;


    /**
     * Endpoint 1 : Génère le fichier Excel d'une conversation et retourne les informations en JSON
     */
    @RequestMapping(value="/export", method=RequestMethod.POST,
            consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<Response<ConversationDto>> generateConversationExport(
            @RequestBody Request<ConversationDto> request) {

        HttpHeaders headers = new HttpHeaders();
        log.info("start method /conversation/export");

        Response<ConversationDto> response = new Response<ConversationDto>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");

        try {
            // Validation de la requête
            response = Validate.validateList(request, response, functionalError, locale);

            if (response.isHasError()) {
                log.warning("Validation failed: " + response.getStatus().getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Génération du fichier Excel
            response = conversationBusiness.exportConversation(request, locale);

            if (response.isHasError()) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Export generated successfully. File: " + response.getFileName() +
                    ", Count: " + response.getCount());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);

        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("end method /conversation/export");
        }
    }

    /**
     * Endpoint 2 : Télécharge le fichier Excel généré
     */
    @RequestMapping(value="/download/{fileName}", method=RequestMethod.GET)
    public ResponseEntity<Object> downloadConversationExportFile(@PathVariable String fileName) {
        log.info("start method /conversation/download/" + fileName);

        try {
            // Validation du nom de fichier
            if (!fileName.matches("^CONVERSATION_EXPORT_\\d{8}_\\d{6}\\.xlsx$")) {
                log.warning("Invalid file name format: " + fileName);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("hasError", true);
                errorResponse.put("code", "INVALID_FILE_NAME");
                errorResponse.put("message", "Format de nom de fichier invalide");
                errorResponse.put("fileName", fileName);

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            // Construction du chemin complet
            String filePath = paramsUtils.getExportPath() + File.separator + fileName;
            File file = new File(filePath);

            // Vérification de l'existence
            if (!file.exists()) {
                log.warning("File not found: " + filePath);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("hasError", true);
                errorResponse.put("code", "FILE_NOT_FOUND");
                errorResponse.put("message", "Le fichier demandé n'existe pas ou a expiré");
                errorResponse.put("fileName", fileName);

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            if (!file.isFile()) {
                log.warning("Path is not a file: " + filePath);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("hasError", true);
                errorResponse.put("code", "INVALID_FILE_TYPE");
                errorResponse.put("message", "Le chemin spécifié n'est pas un fichier valide");
                errorResponse.put("fileName", fileName);

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            // Création de la ressource
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(fileName)
                    .build());
            headers.setContentLength(file.length());
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            log.info("File download started: " + fileName + " (size: " + file.length() + " bytes)");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (FileNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("hasError", true);
            errorResponse.put("code", "FILE_NOT_FOUND");
            errorResponse.put("message", "Impossible d'accéder au fichier");
            errorResponse.put("fileName", fileName);
            errorResponse.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("hasError", true);
            errorResponse.put("code", "DOWNLOAD_ERROR");
            errorResponse.put("message", "Une erreur est survenue lors du téléchargement");
            errorResponse.put("fileName", fileName);
            errorResponse.put("error", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);

        } finally {
            log.info("end method /conversation/download/" + fileName);
        }
    }

    /**
     * Endpoint : Génère un ZIP contenant tous les exports des conversations de l'utilisateur
     */
    @RequestMapping(value="/export/all", method=RequestMethod.POST,
            consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<Response<ConversationDto>> generateAllConversationsExport(
            @RequestBody Request<ConversationDto> request) {

        HttpHeaders headers = new HttpHeaders();
        log.info("start method /conversation/export/all");

        Response<ConversationDto> response = new Response<ConversationDto>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");

        try {
            // Validation
            response = Validate.validateList(request, response, functionalError, locale);

            if (response.isHasError()) {
                log.warning("Validation failed: " + response.getStatus().getMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Génération du ZIP
            response = conversationBusiness.exportAllConversations(request, locale);

            if (response.isHasError()) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("Export ZIP generated successfully. File: " + response.getFileName() +
                    ", Count: " + response.getCount());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);

        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("end method /conversation/export/all");
        }
    }

    /**
     * Endpoint : Télécharge le fichier ZIP généré
     */
    @RequestMapping(value="/download/zip/{fileName}", method=RequestMethod.GET)
    public ResponseEntity<Object> downloadAllConversationsZip(@PathVariable String fileName) {
        log.info("start method /conversation/download/zip/" + fileName);

        try {
            // Validation du nom de fichier
            if (!fileName.matches("^CONVERSATIONS_EXPORT_\\d{8}_\\d{6}\\.zip$")) {
                log.warning("Invalid file name format: " + fileName);

                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("hasError", true);
                errorResponse.put("code", "INVALID_FILE_NAME");
                errorResponse.put("message", "Format de nom de fichier invalide");

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            String filePath = paramsUtils.getExportPath() + File.separator + fileName;
            File file = new File(filePath);

            if (!file.exists() || !file.isFile()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("hasError", true);
                errorResponse.put("code", "FILE_NOT_FOUND");
                errorResponse.put("message", "Le fichier demandé n'existe pas");

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/zip"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(fileName)
                    .build());
            headers.setContentLength(file.length());

            log.info("ZIP download started: " + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("hasError", true);
            errorResponse.put("code", "DOWNLOAD_ERROR");
            errorResponse.put("message", "Erreur lors du téléchargement");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);

        } finally {
            log.info("end method /conversation/download/zip/" + fileName);
        }
    }

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
