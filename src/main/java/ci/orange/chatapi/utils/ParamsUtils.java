
/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

/**
 * Params Utils
 *
 * @author Geo
 * update by ziet
 */

@Component
@Getter
@NoArgsConstructor
@ToString
public class ParamsUtils {
    //vous devez rajouté l'annotation @Value("${}") pour chaque
    //propriété déclaré dans la classe ParamsUtils; les variables ont été définit dans properties au préalable
//    @Value("${}")
	private String apacheRootFilesPath;
//	@Value("${}")
	private String  rootFilesPath;
//    @Value("${}")
    private String apacheRootUrl;

    @Value("${file.upload.base-path}")
    private String baseUploadPath;

    @Value("${file.upload.base-url}")
    private String baseUrl;

    public String getFullDirectory(@NonNull String path) {
        String rootPath = getRootFilesPath();
        return Utilities.createFullPath(rootPath, path);
    }

	public String getFullImageDirectory(boolean useApachePath) {
        String rootPath = useApachePath ? getApacheRootFilesPath() : getRootFilesPath();
        return Utilities.createFullPath(rootPath, "");
    }
}


