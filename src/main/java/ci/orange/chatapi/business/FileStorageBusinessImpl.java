package ci.orange.chatapi.business;

import ci.orange.chatapi.utils.ExceptionUtils;
import ci.orange.chatapi.utils.ParamsUtils;
import ci.orange.chatapi.utils.contract.Response;
import ci.orange.chatapi.utils.dto.UploadFileDto;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Component
@Log
public class FileStorageBusinessImpl implements FIleStorageBusiness {

    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private ParamsUtils paramsUtils;


    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
    );


        @Override
        public String upload(MultipartFile file, Locale locale) {

            Response<UploadFileDto> response = new Response<UploadFileDto>();
            String fileName = null;
            try {

                validateFile(file);

                Path uploadDir = Paths.get(paramsUtils.getBaseUploadPath() , "messages");
                createDirectoryIfNotExist(uploadDir);

                String extension = getExtension(file.getOriginalFilename());
                fileName = UUID.randomUUID() + "." + extension;

                Path targetPath = uploadDir.resolve(fileName);

                try {
                    Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Erreur lors de l'upload de l'image", e);
                }

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
                    log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                    throw new RuntimeException(response.getStatus().getCode() + ";" + response.getStatus().getMessage());
                }
            }


            return paramsUtils.getBaseUrl() + "/messages/" + fileName;


        }

        /* ==================== VALIDATIONS ==================== */

        private void validateFile(MultipartFile file) {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Fichier image obligatoire");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("Image trop volumineuse");
            }

            if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
                throw new RuntimeException("Type d'image non autorisé");
            }
        }

        private void createDirectoryIfNotExist(Path path) {
            try {
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            } catch (IOException e) {
                throw new RuntimeException("Impossible de créer le dossier d'upload", e);
            }
        }

        private String getExtension(String filename) {
            if (filename == null || !filename.contains(".")) {
                throw new RuntimeException("Nom de fichier invalide");
            }
            return filename.substring(filename.lastIndexOf(".") + 1);
        }


}
