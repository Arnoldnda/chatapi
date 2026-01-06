package ci.orange.chatapi.business;

import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public interface FIleStorageBusiness {
    /**
     * Upload une image et retourne son URL publique
     */
    String upload(MultipartFile file, Locale locale);
}

