package kr.hs.dgsw.camfit.photo.dto;

import kr.hs.dgsw.camfit.photo.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PhotoResponseDTO {

    private Long fileId;

    public PhotoResponseDTO(Photo photo) {
        this.fileId = photo.getId();
    }
}
