package kr.hs.dgsw.camfit.member.dto;

import kr.hs.dgsw.camfit.authority.dto.AuthorityDTO;
import kr.hs.dgsw.camfit.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    // 웹이 값 주는거 아님
    private Set<AuthorityDTO> authorityDTOSet;

    /*@Builder
    public MemberInsertDTO(String username, String password, String gender, String dateOfBirth, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }*/

    public static MemberInsertDTO from(Member member) {

        if(member == null) {
            return null;
        }

        return MemberInsertDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .phoneNumber(member.getPhoneNumber())
                .authorityDTOSet(member.getAuthorities().stream()
                        .map(authority -> AuthorityDTO.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
