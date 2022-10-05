package kr.hs.dgsw.camfit.camp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CampInsertDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String place;

    @NotNull
    private int money;

    @NotBlank
    private String explanation;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private short roomCount;

    @NotBlank
    private String area;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    @NotNull
    private Long memberId;

    @Builder
    public CampInsertDTO(String name, String place, int money, String explanation, String phoneNumber, int roomCount, String area, String startDate, String endDate, Long memberId) {
        this.name = name;
        this.place = place;
        this.money = money;
        this.explanation = explanation;
        this.phoneNumber = phoneNumber;
        this.roomCount = (short) roomCount;
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberId = memberId;
    }
}
