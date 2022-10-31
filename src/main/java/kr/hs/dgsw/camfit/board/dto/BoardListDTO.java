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

    private Long thumbnailId; //썸네일 id

    @NotNull
    private String regdate;

    @NotBlank
    private String username;

    @Builder
    public BoardListDTO(Long id, String title, String content, Long thumbnailId ,String regdate, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnailId = thumbnailId;
        this.regdate = regdate;
        this.username = username;
    }
}
