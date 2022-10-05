package kr.hs.dgsw.camfit.photo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PhotoInsertDTO {

    @NotBlank
    private String origFileName;

    @NotBlank
    private String filePath;

    @NotNull
    private Long fileSize;

    @Builder
    public PhotoInsertDTO(String origFileName, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
