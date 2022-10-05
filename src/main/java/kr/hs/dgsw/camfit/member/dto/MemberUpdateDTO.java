package kr.hs.dgsw.camfit.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberUpdateDTO {

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

    @NotNull
    private Long memberId;

    @Builder
    public MemberUpdateDTO(String username, String password, String gender, String dateOfBirth, String phoneNumber, Long memberId) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.memberId = memberId;
    }
}
