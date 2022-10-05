package kr.hs.dgsw.camfit.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BoardDeleteDTO {

    @NotNull
    private Long memberId;

    @NotNull
    private Long boardId;

    @Builder
    public BoardDeleteDTO(Long memberId, Long boardId) {
        this.memberId = memberId;
        this.boardId = boardId;
    }
}
