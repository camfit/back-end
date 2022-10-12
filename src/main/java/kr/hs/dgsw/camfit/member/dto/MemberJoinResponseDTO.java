package kr.hs.dgsw.camfit.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class MemberJoinResponseDTO {

    private String username;
    private String password;
    private String token;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;
}
