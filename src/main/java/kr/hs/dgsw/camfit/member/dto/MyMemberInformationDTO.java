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
public class MyMemberInformationDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private LocalDate regdate;

    @NotBlank
    private String gender;

    @NotBlank
    private String dateOfBirth;
}
