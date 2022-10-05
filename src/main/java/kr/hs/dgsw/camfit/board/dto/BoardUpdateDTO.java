package kr.hs.dgsw.camfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardUpdateDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long boardId;

    @NotNull
    private Long memberId;

    @Builder
    public BoardUpdateDTO(String title, String content, Long boardId, Long memberId) {
        this.title = title;
        this.content = content;
        this.boardId = boardId;
        this.memberId = memberId;
    }
}
