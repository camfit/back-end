package kr.hs.dgsw.camfit.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReservationUpdateDTO {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private int money;

    @NotNull
    private Long memberId;

    @NotNull
    private Long campId;

    @NotNull
    private Long reservationId;
}
