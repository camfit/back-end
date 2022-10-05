package kr.hs.dgsw.camfit.board.dto;

import kr.hs.dgsw.camfit.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDTO {

    private Long id;
    private String memberName;
    private String title;
    private String content;
    private List<Long> fileId; //첨부 파일 id 목록

    @Builder
    public BoardResponseDTO(Board board, List<Long> fileId) {
        this.id = board.getId();
        this.memberName = board.getMember().getUsername();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.fileId = fileId;
    }
}
