package kr.hs.dgsw.camfit.board.dto;

import kr.hs.dgsw.camfit.photo.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardListDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String photoPath; //사진 경로

    @NotNull
    private String regdate;

    @NotBlank
    private String username;

    @Builder
    public BoardListDTO(Long id, String title, String content, String photoPath ,String regdate, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.photoPath = photoPath;
        this.regdate = regdate;
        this.username = username;
    }
}
