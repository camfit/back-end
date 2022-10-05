package kr.hs.dgsw.camfit.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReservationDeleteDTO {

    @NotNull
    private Long memberId;

    @NotNull
    private Long campId;

    @NotNull
    private Long reservationId;

    @Builder
    public ReservationDeleteDTO(Long memberId, Long campId, Long reservationId) {
        this.memberId = memberId;
        this.campId = campId;
        this.reservationId = reservationId;
    }
}
