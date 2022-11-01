package kr.hs.dgsw.camfit.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "key 값은 boardInsertDTO이고 value는 {\"title\":\"string\", \"content\",\"string\",\"memberId\":1}")
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
