package kr.hs.dgsw.camfit.camp.dto;

import kr.hs.dgsw.camfit.camp.Area;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CampListDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String place;

    @NotNull
    private int money;

    @NotBlank
    private String explanation;

    @NotNull
    private Short roomCount;

    @NotNull
    private Area area;

    @Builder
    public CampListDTO(Long id, String name, String place, int money, String explanation, Short roomCount, Area area) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.money = money;
        this.explanation = explanation;
        this.roomCount = roomCount;
        this.area = area;
    }
}
