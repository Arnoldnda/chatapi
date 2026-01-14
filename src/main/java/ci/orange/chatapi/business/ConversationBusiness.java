                                                    											
/*
 * Java business for entity table conversation 
 * Created on 2026-01-03 ( Time 10:00:03 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.business;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.IBasicBusiness;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.dto.transformer.*;
import ci.orange.chatapi.dao.entity.Conversation;
import ci.orange.chatapi.dao.entity.TypeConversation;
import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.*;
import org.springframework.transaction.annotation.Transactional;

/**
BUSINESS for table "conversation"
 * 
 * @author Geo
 *
 */
@Log
@Component
public class ConversationBusiness implements IBasicBusiness<Request<ConversationDto>, Response<ConversationDto>> {

    private Response<ConversationDto> response;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationUserRepository conversationUserRepository;
    @Autowired
    private TypeConversationRepository typeConversationRepository;
    @Autowired
    private MessageRepository messageRepository;
	@Autowired
	private FunctionalError functionalError;
	@PersistenceContext
	private EntityManager em;
    @Autowired
    private ParamsUtils paramsUtils;
    @Autowired
    private ExceptionUtils exceptionUtils;

	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateTimeFormat;

	public ConversationBusiness() {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}

    public Response<ConversationDto> exportConversation(Request<ConversationDto> request, Locale locale) {
        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Response<ConversationDto> response = new Response<ConversationDto>();

        try {
            log.info("----begin exportConversation-----");

            // Validation
            if (request.getDatas().size() != 1) {
                response.setStatus(functionalError.REQUEST_ERROR(
                        "Une seule conversation peut être exportée à la fois", locale));
                response.setHasError(true);
                return response;
            }

            ConversationDto dto = request.getDatas().get(0);

            // Definir les parametres obligatoires
            Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
            fieldsToVerify.put("id", dto.getId());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }

            Integer conversationId = dto.getId();
            Integer userId = request.getUser();

            // Vérifier que la conversation existe
            Conversation conversation = conversationRepository.findOne(conversationId, false);
            if (conversation == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST(
                        "Conversation inexistante: " + conversationId, locale));
                response.setHasError(true);
                return response;
            }

            // Vérifier que l'utilisateur est membre
            ConversationUser membershipOpt = conversationUserRepository
                    .findActiveUserInConversation(conversationId, userId);

            if (membershipOpt == null ) {
                response.setStatus(functionalError.UNAUTHORIZED(
                        "Vous n'êtes pas membre de cette conversation", locale));
                response.setHasError(true);
                return response;
            }

            // Récupérer les messages visibles pour cet utilisateur
            List<Message> messages = messageRepository.findMessagesForUser(conversationId, userId);

            // Récupérer les participants
            List<ConversationUser> participants = conversationUserRepository
                    .findByConversationId(conversationId, false);

            // Charger le template Excel
            ClassPathResource resource = new ClassPathResource(
                    "templates/excel/conversation_export_template.xlsx");
            InputStream inputStream = resource.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Cell cell = null;
            Row row = null;
            int rowIndex = 0;

            // === SECTION 1 : Informations de la conversation ===
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("INFORMATIONS DE LA CONVERSATION");

            rowIndex++; // Ligne vide

            // Titre
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Titre:");
            cell = Utilities.getCell(row, 1);
            cell.setCellValue(Utilities.isNotBlank(conversation.getTitre())
                    ? conversation.getTitre() : "Sans titre");

            // Type
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Type:");
            cell = Utilities.getCell(row, 1);
            cell.setCellValue(conversation.getTypeConversation().getLibelle());

            // Créateur
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Créé par:");
            cell = Utilities.getCell(row, 1);
            User creator = userRepository.findOne(conversation.getCreatedBy(), false);
            if (creator != null) {
                cell.setCellValue(creator.getNom() + " " + creator.getPrenoms());
            } else {
                cell.setCellValue("Inconnu");
            }

            // Date de création
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Date de création:");
            cell = Utilities.getCell(row, 1);
            cell.setCellValue(conversation.getCreatedAt() != null
                    ? sdfDate.format(conversation.getCreatedAt()) : "");

            rowIndex++; // Ligne vide

            // === SECTION 2 : Participants ===
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("PARTICIPANTS (" + participants.size() + ")");

            rowIndex++; // Ligne vide

            // En-têtes participants
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Nom");
            cell = Utilities.getCell(row, 1);
            cell.setCellValue("Prénoms");
            cell = Utilities.getCell(row, 2);
            cell.setCellValue("Rôle");
            cell = Utilities.getCell(row, 3);
            cell.setCellValue("Date Intégration");
            cell = Utilities.getCell(row, 4);
            cell.setCellValue("Statut");

            // Liste des participants
            for (ConversationUser participant : participants) {
                row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);

                User user = participant.getUser();

                cell = Utilities.getCell(row, 0);
                cell.setCellValue(user.getNom() != null ? user.getNom() : "");

                cell = Utilities.getCell(row, 1);
                cell.setCellValue(user.getPrenoms() != null ? user.getPrenoms() : "");

                cell = Utilities.getCell(row, 2);
                cell.setCellValue(Utilities.isTrue(participant.getRole())
                        ? "Administrateur" : "Membre");

                cell = Utilities.getCell(row, 3);
                cell.setCellValue(participant.getCreatedAt() != null
                        ? sdfDate.format(conversation.getCreatedAt()) : "");

                cell = Utilities.getCell(row, 4);
                String status = "Actif";
                if (Utilities.isTrue(participant.getHasDefinitivelyLeft())) {
                    status = "Quitté définitivement";
                } else if (Utilities.isTrue(participant.getHasLeft())) {
                    status = "Quitté";
                }
                cell.setCellValue(status);

                rowIndex++;
            }

            rowIndex += 2; // Lignes vides

            // === SECTION 3 : Messages ===
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("MESSAGES (" + messages.size() + ")");

            rowIndex++; // Ligne vide

            // En-têtes messages
            row = sheet.getRow(rowIndex++);
            if (row == null) row = sheet.createRow(rowIndex - 1);
            cell = Utilities.getCell(row, 0);
            cell.setCellValue("Date");
            cell = Utilities.getCell(row, 1);
            cell.setCellValue("Auteur");
            cell = Utilities.getCell(row, 2);
            cell.setCellValue("Type");
            cell = Utilities.getCell(row, 3);
            cell.setCellValue("Contenu");
            cell = Utilities.getCell(row, 4);
            cell.setCellValue("Image URL");

            // Liste des messages
            for (Message message : messages) {
                row = sheet.getRow(rowIndex);
                if (row == null) row = sheet.createRow(rowIndex);

                cell = Utilities.getCell(row, 0);
                cell.setCellValue(message.getCreatedAt() != null
                        ? sdfDate.format(message.getCreatedAt()) : "");

                cell = Utilities.getCell(row, 1);
                User author = userRepository.findOne(message.getCreatedBy(), false);
                if (author != null) {
                    cell.setCellValue(author.getNom() + " " + author.getPrenoms());
                } else {
                    cell.setCellValue("Inconnu");
                }

                cell = Utilities.getCell(row, 2);
                cell.setCellValue(message.getTypeMessage2() != null
                        ? message.getTypeMessage2().getLibelle() : "");

                cell = Utilities.getCell(row, 3);
                cell.setCellValue(Utilities.isNotBlank(message.getContent())
                        ? message.getContent() : "");

                cell = Utilities.getCell(row, 4);
                cell.setCellValue(Utilities.isNotBlank(message.getImgUrl())
                        ? message.getImgUrl() : "");

                rowIndex++;
            }

            inputStream.close();

            // Générer le nom du fichier
            String fileName = "CONVERSATION_EXPORT_" + sdfFileName.format(new Date()) + ".xlsx";
            String filePath = paramsUtils.getExportPath() + File.separator + fileName;

            // Écrire le fichier
            FileOutputStream outFile = new FileOutputStream(filePath);
            workbook.write(outFile);
            outFile.close();
            workbook.close();

            response.setHasError(Boolean.FALSE);
            response.setCount((long) messages.size());
            response.setStatus(functionalError.SUCCESS("Export généré avec succès", locale));
            response.setFileName(fileName);

            log.info("----end exportConversation-----");

        } catch (PermissionDeniedDataAccessException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (DataAccessResourceFailureException e) {
            exceptionUtils.DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(response, locale, e);
        } catch (DataAccessException e) {
            exceptionUtils.DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        } finally {
            if (response.isHasError() && response.getStatus() != null) {
                log.info(String.format("Erreur| code: {} - message: {}",
                        response.getStatus().getCode(), response.getStatus().getMessage()));
                throw new RuntimeException(response.getStatus().getCode() + ";" +
                        response.getStatus().getMessage());
            }
        }
        return response;
    }

    public Response<ConversationDto> exportAllConversations(Request<ConversationDto> request, Locale locale) {
        SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Response<ConversationDto> response = new Response<ConversationDto>();
        List<String> generatedFiles = new ArrayList<>();

        try {
            log.info("----begin exportAllConversations-----");

            Integer userId = request.getUser();

            // Récupérer toutes les conversations active de l'utilisateur
            List<ConversationUser> userConversations = conversationUserRepository
                    .findActiveConversationsByUser(userId);

            if (Utilities.isEmpty(userConversations)) {
                response.setStatus(functionalError.DATA_EMPTY(
                        "Aucune conversation à exporter", locale));
                response.setHasError(false);
                return response;
            }

            log.info("Exporting " + userConversations.size() + " conversations for user " + userId);

            // Charger le template
            ClassPathResource resource = new ClassPathResource(
                    "templates/excel/conversation_export_template.xlsx");

            // Pour chaque conversation, générer un fichier Excel
            int fileCount = 1;
            for (ConversationUser userConv : userConversations) {
                Conversation conversation = userConv.getConversation();

                // Skip si conversation supprimée
                if (Utilities.isTrue(conversation.getIsDeleted())) {
                    continue;
                }

                try {
                    InputStream inputStream = resource.getInputStream();
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    Cell cell = null;
                    Row row = null;
                    int rowIndex = 0;

                    // === INFORMATIONS CONVERSATION ===
                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("INFORMATIONS DE LA CONVERSATION");

                    rowIndex++;

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Titre:");
                    cell = Utilities.getCell(row, 1);
                    cell.setCellValue(Utilities.isNotBlank(conversation.getTitre())
                            ? conversation.getTitre() : "Sans titre");

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Type:");
                    cell = Utilities.getCell(row, 1);
                    cell.setCellValue(conversation.getTypeConversation().getLibelle());

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Créé par:");
                    cell = Utilities.getCell(row, 1);
                    User creator = userRepository.findOne(conversation.getCreatedBy(), false);
                    cell.setCellValue(creator != null
                            ? creator.getNom() + " " + creator.getPrenoms() : "Inconnu");

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Date de création:");
                    cell = Utilities.getCell(row, 1);
                    cell.setCellValue(conversation.getCreatedAt() != null
                            ? sdfDate.format(conversation.getCreatedAt()) : "");

                    rowIndex += 2;

                    // === PARTICIPANTS ===
                    List<ConversationUser> participants = conversationUserRepository
                            .findByConversationId(conversation.getId(), false);

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("PARTICIPANTS (" + participants.size() + ")");

                    rowIndex++;

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Nom");
                    cell = Utilities.getCell(row, 1);
                    cell.setCellValue("Prénoms");
                    cell = Utilities.getCell(row, 2);
                    cell.setCellValue("Rôle");
                    cell = Utilities.getCell(row, 3);
                    cell.setCellValue("Date Intégration");
                    cell = Utilities.getCell(row, 4);
                    cell.setCellValue("Statut");

                    for (ConversationUser participant : participants) {
                        row = sheet.getRow(rowIndex);
                        if (row == null) row = sheet.createRow(rowIndex);

                        User user = participant.getUser();

                        cell = Utilities.getCell(row, 0);
                        cell.setCellValue(user.getNom() != null ? user.getNom() : "");

                        cell = Utilities.getCell(row, 1);
                        cell.setCellValue(user.getPrenoms() != null ? user.getPrenoms() : "");

                        cell = Utilities.getCell(row, 2);
                        cell.setCellValue(Utilities.isTrue(participant.getRole())
                                ? "Administrateur" : "Membre");

                        cell = Utilities.getCell(row, 3);
                        cell.setCellValue(participant.getCreatedAt() != null
                                ? sdfDate.format(conversation.getCreatedAt()) : "");

                        cell = Utilities.getCell(row, 4);
                        String status = "Actif";
                        if (Utilities.isTrue(participant.getHasDefinitivelyLeft())) {
                            status = "Quitté définitivement";
                        } else if (Utilities.isTrue(participant.getHasLeft())) {
                            status = "Quitté";
                        }
                        cell.setCellValue(status);

                        rowIndex++;
                    }

                    rowIndex += 2;

                    // === MESSAGES ===
                    List<Message> messages = messageRepository
                            .findMessagesForUser(conversation.getId(), userId);

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("MESSAGES (" + messages.size() + ")");

                    rowIndex++;

                    row = sheet.getRow(rowIndex++);
                    if (row == null) row = sheet.createRow(rowIndex - 1);
                    cell = Utilities.getCell(row, 0);
                    cell.setCellValue("Date");
                    cell = Utilities.getCell(row, 1);
                    cell.setCellValue("Auteur");
                    cell = Utilities.getCell(row, 2);
                    cell.setCellValue("Type");
                    cell = Utilities.getCell(row, 3);
                    cell.setCellValue("Contenu");
                    cell = Utilities.getCell(row, 4);
                    cell.setCellValue("Image URL");

                    for (Message message : messages) {
                        row = sheet.getRow(rowIndex);
                        if (row == null) row = sheet.createRow(rowIndex);

                        cell = Utilities.getCell(row, 0);
                        cell.setCellValue(message.getCreatedAt() != null
                                ? sdfDate.format(message.getCreatedAt()) : "");

                        cell = Utilities.getCell(row, 1);
                        User author = userRepository.findOne(message.getCreatedBy(), false);
                        cell.setCellValue(author != null
                                ? author.getNom() + " " + author.getPrenoms() : "Inconnu");

                        cell = Utilities.getCell(row, 2);
                        cell.setCellValue(message.getTypeMessage2() != null
                                ? message.getTypeMessage2().getLibelle() : "");

                        cell = Utilities.getCell(row, 3);
                        cell.setCellValue(Utilities.isNotBlank(message.getContent())
                                ? message.getContent() : "");

                        cell = Utilities.getCell(row, 4);
                        cell.setCellValue(Utilities.isNotBlank(message.getImgUrl())
                                ? message.getImgUrl() : "");

                        rowIndex++;
                    }

                    inputStream.close();

                    // Générer un nom de fichier unique pour cette conversation
                    String sanitizedTitle = conversation.getTitre() != null
                            ? conversation.getTitre().replaceAll("[^a-zA-Z0-9]", "_")
                            : "Conversation_" + conversation.getId();

                    String individualFileName = fileCount + "_" + sanitizedTitle + ".xlsx";
                    String individualFilePath = paramsUtils.getExportPath() + File.separator +
                            individualFileName;

                    FileOutputStream outFile = new FileOutputStream(individualFilePath);
                    workbook.write(outFile);
                    outFile.close();
                    workbook.close();

                    generatedFiles.add(individualFilePath);
                    fileCount++;

                } catch (Exception e) {
                    log.warning("Error exporting conversation " + conversation.getId() + ": " +
                            e.getMessage());
                    // Continuer avec les autres conversations
                }
            }

            if (generatedFiles.isEmpty()) {
                response.setStatus(functionalError.DATA_EMPTY(
                        "Aucune conversation n'a pu être exportée", locale));
                response.setHasError(true);
                return response;
            }

            // Créer le fichier ZIP
            String zipFileName = "CONVERSATIONS_EXPORT_" + sdfFileName.format(new Date()) + ".zip";
            String zipFilePath = paramsUtils.getExportPath() + File.separator + zipFileName;

            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String filePath : generatedFiles) {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);

                ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                fis.close();
                zos.closeEntry();

                // Supprimer le fichier Excel individuel après l'avoir zippé
                file.delete();
            }

            zos.close();
            fos.close();

            response.setHasError(Boolean.FALSE);
            response.setCount((long) generatedFiles.size());
            response.setStatus(functionalError.SUCCESS(
                    "Export ZIP généré avec succès (" + generatedFiles.size() + " conversations)",
                    locale));
            response.setFileName(zipFileName);

            log.info("----end exportAllConversations-----");

        } catch (PermissionDeniedDataAccessException e) {
            exceptionUtils.PERMISSION_DENIED_DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (DataAccessResourceFailureException e) {
            exceptionUtils.DATA_ACCESS_RESOURCE_FAILURE_EXCEPTION(response, locale, e);
        } catch (DataAccessException e) {
            exceptionUtils.DATA_ACCESS_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        } finally {
            // Nettoyer les fichiers temporaires en cas d'erreur
            if (response.isHasError()) {
                for (String filePath : generatedFiles) {
                    try {
                        new File(filePath).delete();
                    } catch (Exception ignored) {}
                }
            }

            if (response.isHasError() && response.getStatus() != null) {
                log.info(String.format("Erreur| code: {} - message: {}",
                        response.getStatus().getCode(), response.getStatus().getMessage()));
                throw new RuntimeException(response.getStatus().getCode() + ";" +
                        response.getStatus().getMessage());
            }
        }
        return response;
    }
	
	/**
	 * create Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
    public Response<ConversationDto> create(Request<ConversationDto> request, Locale locale)  throws ParseException {
		log.info("----begin create Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();

			fieldsToVerify.put("participantIds", dto.getParticipantIds());
            fieldsToVerify.put("titre", dto.getTitre());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

             /*
              contrainte Générales.
            */

            // verifier si la liste des participants n'est pas vide
            if  (dto.getParticipantIds().isEmpty()) {
                response.setStatus(functionalError.FIELD_EMPTY("Cette liste ne dois pas être vide", locale));
                response.setHasError(true);
                return response;
            }

            // verifier que l'utilisateur existe bien.
            User actor = userRepository.findOne(request.getUser(), false);
            if (actor == null ) {
                response.setStatus(functionalError.DATA_NOT_EXIST(
                        "Ce utilisateur n'existe pas. UserId : " + request.getUser(), locale));
                response.setHasError(true);
                return response;
            }

            // verifier que le créateur de la conversation n'est pas inclus dans la liste des participants
            if (dto.getParticipantIds().contains(request.getUser())) {
                response.setStatus(functionalError.REQUEST_ERROR(
                        "Le créateur ne doit pas être inclus dans la liste des participants",
                        locale
                ));
                response.setHasError(true);
                return response;
            }

            // vérifier qu'un utilisateur n'est pas ajouté deux fois ou plus
            if (dto.getParticipantIds().size() != new HashSet<>(dto.getParticipantIds()).size()) {
                response.setStatus(functionalError.REQUEST_ERROR(
                        "La liste des participants contient des doublons", locale));
                response.setHasError(true);
                return response;
            }

            // recupéré le type de conversation de groupe
            TypeConversation existingTypeConversation = null;
            existingTypeConversation = typeConversationRepository.findByCode("GROUP", false);
            if (existingTypeConversation == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le Type de conversation invalide. Autorisé : PRIVATE, GROUP" + dto.getTypeConversationId(), locale));
                response.setHasError(true);
                return response;
            }

            // persistance de la conversation
            Conversation entityToSave = null;
			entityToSave = ConversationTransformer.INSTANCE.toEntity(dto, existingTypeConversation);
			entityToSave.setCreatedAt(Utilities.getCurrentDate());
			entityToSave.setCreatedBy(request.getUser());
			entityToSave.setIsDeleted(false);

            // pour pourvoir utilisé l'id générer
            Conversation entitySaved = conversationRepository.save(entityToSave);

            log.info("----begin Ajout member in conversation (manual) -----");

            List<ConversationUser> participants = new ArrayList<>();

            //  Ajout du créateur de la conversation
            ConversationUser creator = new ConversationUser();
            creator.setConversation(entitySaved);
            creator.setUser(actor);
            // rôle : admin
            creator.setRole(true);
            creator.setHasLeft(false);
            creator.setHasDefinitivelyLeft(false);
            creator.setHasCleaned(false);
            creator.setIsDeleted(false);
            creator.setCreatedAt(Utilities.getCurrentDate());
            creator.setCreatedBy(actor.getId());

            participants.add(creator);

            // Ajout des participants
            for (Integer participantId : dto.getParticipantIds()) {

                User participant = userRepository.findOne(participantId, false);
                if (participant == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Ce participant n'existe pas." + participantId, locale));
                    response.setHasError(true);
                    return response;
                }

                ConversationUser participant1 = new ConversationUser();
                participant1.setConversation(entitySaved);
                participant1.setUser(participant);
                participant1.setRole(false); // simple membre

                participant1.setHasLeft(false);
                participant1.setHasDefinitivelyLeft(false);
                participant1.setHasCleaned(false);
                participant1.setIsDeleted(false);

                participant1.setCreatedAt(Utilities.getCurrentDate());
                participant1.setCreatedBy(actor.getId());

                participants.add(participant1);
            }

            conversationUserRepository.saveAll(participants);

            log.info("----end Ajout member in conversation (manual) -----");

            items.add(entitySaved);
		}

		if (!items.isEmpty()) {
			List<Conversation> itemsSaved = null;
			// inserer les donnees en base de donnees
			itemsSaved = conversationRepository.saveAll((Iterable<Conversation>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversation", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationTransformer.INSTANCE.toDtos(itemsSaved);

			final int size = itemsSaved.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setHasError(false);
		}

		log.info("----end create Conversation-----");
		return response;
	}

	/**
	 * update Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> update(Request<ConversationDto> request, Locale locale)  throws ParseException {
		log.info("----begin update Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversation existe
			Conversation entityToSave = null;
			entityToSave = conversationRepository.findOne(dto.getId(), false);
			if (entityToSave == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversation id -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// Verify if typeConversation exist
			if (dto.getTypeConversationId() != null && dto.getTypeConversationId() > 0){
				TypeConversation existingTypeConversation = typeConversationRepository.findOne(dto.getTypeConversationId(), false);
				if (existingTypeConversation == null) {
					response.setStatus(functionalError.DATA_NOT_EXIST("typeConversation typeConversationId -> " + dto.getTypeConversationId(), locale));
					response.setHasError(true);
					return response;
				}
				entityToSave.setTypeConversation(existingTypeConversation);
			}
			if (Utilities.notBlank(dto.getTitre())) {
				entityToSave.setTitre(dto.getTitre());
			}
			if (Utilities.notBlank(dto.getDeletedAt())) {
				entityToSave.setDeletedAt(dateFormat.parse(dto.getDeletedAt()));
			}
			if (dto.getCreatedBy() != null && dto.getCreatedBy() > 0) {
				entityToSave.setCreatedBy(dto.getCreatedBy());
			}
			if (dto.getUpdatedBy() != null && dto.getUpdatedBy() > 0) {
				entityToSave.setUpdatedBy(dto.getUpdatedBy());
			}
			if (dto.getDeletedBy() != null && dto.getDeletedBy() > 0) {
				entityToSave.setDeletedBy(dto.getDeletedBy());
			}
			entityToSave.setUpdatedAt(Utilities.getCurrentDate());
			entityToSave.setUpdatedBy(request.getUser());
			items.add(entityToSave);
		}

		if (!items.isEmpty()) {
			List<Conversation> itemsSaved = null;
			// maj les donnees en base
			itemsSaved = conversationRepository.saveAll((Iterable<Conversation>) items);
			if (itemsSaved == null) {
				response.setStatus(functionalError.SAVE_FAIL("conversation", locale));
				response.setHasError(true);
				return response;
			}
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(itemsSaved) : ConversationTransformer.INSTANCE.toDtos(itemsSaved);

			final int size = itemsSaved.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setHasError(false);
		}

		log.info("----end update Conversation-----");
		return response;
	}

	/**
	 * delete Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> delete(Request<ConversationDto> request, Locale locale)  {
		log.info("----begin delete Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation>        items    = new ArrayList<Conversation>();
			
		for (ConversationDto dto : request.getDatas()) {
			// Definir les parametres obligatoires
			Map<String, java.lang.Object> fieldsToVerify = new HashMap<String, java.lang.Object>();
			fieldsToVerify.put("id", dto.getId());
			if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
				response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
				response.setHasError(true);
				return response;
			}

			// Verifier si la conversation existe
			Conversation existingEntity = null;

			existingEntity = conversationRepository.findOne(dto.getId(), false);
			if (existingEntity == null) {
				response.setStatus(functionalError.DATA_NOT_EXIST("conversation -> " + dto.getId(), locale));
				response.setHasError(true);
				return response;
			}

			// -----------------------------------------------------------------------
			// ----------- CHECK IF DATA IS USED
			// -----------------------------------------------------------------------

			// conversationUser
			List<ConversationUser> listOfConversationUser = conversationUserRepository.findByConversationId(existingEntity.getId(), false);
			if (listOfConversationUser != null && !listOfConversationUser.isEmpty()){
				response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfConversationUser.size() + ")", locale));
				response.setHasError(true);
				return response;
			}
			// message
			List<Message> listOfMessage = messageRepository.findByConversationId(existingEntity.getId(), false);
			if (listOfMessage != null && !listOfMessage.isEmpty()){
				response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfMessage.size() + ")", locale));
				response.setHasError(true);
				return response;
			}


			existingEntity.setDeletedAt(Utilities.getCurrentDate());
			existingEntity.setDeletedBy(request.getUser());
			existingEntity.setIsDeleted(true);
			items.add(existingEntity);
		}

		if (!items.isEmpty()) {
			// supprimer les donnees en base
			conversationRepository.saveAll((Iterable<Conversation>) items);

			response.setHasError(false);
		}

		log.info("----end delete Conversation-----");
		return response;
	}

	/**
	 * get Conversation by using ConversationDto as object.
	 * 
	 * @param request
	 * @return response
	 * 
	 */
	@Override
	public Response<ConversationDto> getByCriteria(Request<ConversationDto> request, Locale locale)  throws Exception {
		log.info("----begin get Conversation-----");

		Response<ConversationDto> response = new Response<ConversationDto>();
		List<Conversation> items 			 = conversationRepository.getByCriteria(request, em, locale);

		if (items != null && !items.isEmpty()) {
			List<ConversationDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? ConversationTransformer.INSTANCE.toLiteDtos(items) : ConversationTransformer.INSTANCE.toDtos(items);

			final int size = items.size();
			List<String>  listOfError      = Collections.synchronizedList(new ArrayList<String>());
			itemsDto.parallelStream().forEach(dto -> {
				try {
					dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
				} catch (Exception e) {
					listOfError.add(e.getMessage());
					e.printStackTrace();
				}
			});
			if (Utilities.isNotEmpty(listOfError)) {
				Object[] objArray = listOfError.stream().distinct().toArray();
				throw new RuntimeException(StringUtils.join(objArray, ", "));
			}
			response.setItems(itemsDto);
			response.setCount(conversationRepository.count(request, em, locale));
			response.setHasError(false);
		} else {
			response.setStatus(functionalError.DATA_EMPTY("conversation", locale));
			response.setHasError(false);
			return response;
		}

		log.info("----end get Conversation-----");
		return response;
	}

	/**
	 * get full ConversationDto by using Conversation as object.
	 * 
	 * @param dto
	 * @param size
	 * @param isSimpleLoading
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ConversationDto getFullInfos(ConversationDto dto, Integer size, Boolean isSimpleLoading, Locale locale) throws Exception {
		// put code here

		if (Utilities.isTrue(isSimpleLoading)) {
			return dto;
		}
		if (size > 1) {
			return dto;
		}

		return dto;
	}
}
