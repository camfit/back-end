package kr.hs.dgsw.camfit.reservation.repository;

import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.reservation.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    public List<Reservation> findByMember(Member member, Pageable pageable);
}
