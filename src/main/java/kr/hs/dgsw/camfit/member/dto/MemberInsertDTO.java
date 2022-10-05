package kr.hs.dgsw.camfit.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberInsertDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String gender;

    @NotBlank
    private String dateOfBirth;

    @NotBlank
    private String phoneNumber;

    @Builder
    public MemberInsertDTO(String username, String password, String gender, String dateOfBirth, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }
}
