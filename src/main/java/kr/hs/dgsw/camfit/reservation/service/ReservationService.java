package kr.hs.dgsw.camfit.reservation.service;

import kr.hs.dgsw.camfit.reservation.Reservation;
import kr.hs.dgsw.camfit.reservation.dto.ReservationDeleteDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationInsertDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    public Reservation insert(ReservationInsertDTO reservationInsertDTO);
    public Reservation update(ReservationUpdateDTO reservationUpdateDTO);
    public Long delete(ReservationDeleteDTO reservationDeleteDTO);

    List<Reservation> reservationList(Long memberId, Pageable pageable);
}
