package kr.hs.dgsw.camfit.photo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhotoListDTO {

    private String origFileName;
    private String filePath;
    private Long fileSize;

    @Builder
    public PhotoListDTO(String origFileName, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
