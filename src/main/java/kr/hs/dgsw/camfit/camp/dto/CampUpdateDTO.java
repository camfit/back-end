package kr.hs.dgsw.camfit.camp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CampUpdateDTO {

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

    @NotNull
    private Long campId;

    @Builder
    public CampUpdateDTO(String name, String place, int money, String explanation, String phoneNumber, int roomCount, String area, String startDate, String endDate, Long campId, Long memberId) {
        this.name = name;
        this.place = place;
        this.money = money;
        this.explanation = explanation;
        this.phoneNumber = phoneNumber;
        this.roomCount = (short) roomCount;
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
        this.campId = campId;
        this.memberId = memberId;
    }

}
