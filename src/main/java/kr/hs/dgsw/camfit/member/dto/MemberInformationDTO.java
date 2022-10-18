package kr.hs.dgsw.camfit.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class MemberInformationDTO {

    @NotBlank
    private String username;

    @NotNull
    private LocalDate regdate;

    @NotBlank
    private String gender;

    @NotBlank
    private String dateOfBirth;

}
