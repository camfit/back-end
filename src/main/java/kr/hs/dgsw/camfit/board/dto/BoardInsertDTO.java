package kr.hs.dgsw.camfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardInsertDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long memberId;

    @Builder
    public BoardInsertDTO(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
