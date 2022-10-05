package kr.hs.dgsw.camfit.camp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CampDeleteDTO {

    @NotNull
    private Long memberId;

    @NotNull
    private Long campId;

    @Builder
    public CampDeleteDTO(Long memberId, Long campId) {
        this.memberId = memberId;
        this.campId = campId;
    }
}
