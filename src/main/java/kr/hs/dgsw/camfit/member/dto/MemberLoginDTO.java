package kr.hs.dgsw.camfit.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberLoginDTO {

    @Schema(description = "회원이름", example = "seojun")
    @NotNull
    private String username;

    @Schema(description = "비밀번호", example = "seojun")
    @NotNull
    private String password;
}
