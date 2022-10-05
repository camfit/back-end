package kr.hs.dgsw.camfit.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReservationInsertDTO {

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    @NotNull
    private int money;

    @NotNull
    private Long memberId;

    @NotNull
    private Long campId;

    @Builder
    public ReservationInsertDTO(String startDate, String endDate, int money, Long memberId, Long campId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.money = money;
        this.memberId = memberId;
        this.campId = campId;
    }
}
