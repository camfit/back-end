package kr.hs.dgsw.camfit.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReservationListDTO {

    @NotNull
    private Long id;

    @NotBlank
    private LocalDate startDate;

    @NotBlank
    private LocalDate endDate;

    @NotNull
    private int money;

    @NotBlank
    private String campName;

    @Builder
    public ReservationListDTO(Long id, LocalDate startDate, LocalDate endDate, int money, String campName) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.money = money;
        this.campName = campName;
    }
}
