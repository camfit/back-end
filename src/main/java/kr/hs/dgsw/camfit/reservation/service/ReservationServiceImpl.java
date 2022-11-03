package kr.hs.dgsw.camfit.reservation.service;

import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.repository.CampRepository;
import kr.hs.dgsw.camfit.exception.WrongDateException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.exception.WrongMemberException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.repository.MemberRepository;
import kr.hs.dgsw.camfit.reservation.Reservation;
import kr.hs.dgsw.camfit.reservation.dto.ReservationDeleteDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationInsertDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationUpdateDTO;
import kr.hs.dgsw.camfit.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final CampRepository campRepository;

    @Override
    public Reservation insert(ReservationInsertDTO reservationInsertDTO) {

        log.info("ReservationService insert 실행, ReservationInsertDTO : {}", reservationInsertDTO);

        Member member = getMember(reservationInsertDTO.getMemberId());
        Camp camp = getCamp(reservationInsertDTO.getCampId());

        LocalDate startDate = LocalDate.parse(reservationInsertDTO.getStartDate());
        LocalDate endDate = LocalDate.parse(reservationInsertDTO.getEndDate());

        dateConfirmation(camp, startDate, endDate);

        if(camp.getRoomCount() < 1) {
            log.error("캠핑장에 존재하는 방이 없음");
            throw new WrongDateException("존재하는 방이 없음");
        }

        Reservation reservation = Reservation.builder()
                .startDate(startDate)
                .endDate(endDate)
                .money(Period.between(startDate, endDate).getDays() * reservationInsertDTO.getMoney())
                .member(member)
                .camp(camp)
                .build();

        camp.reservationCamp();

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation update(ReservationUpdateDTO reservationUpdateDTO) {

        log.info("ReservationService update 실행, ReservationUpdateDTO : {}", reservationUpdateDTO);

        Reservation reservation = getReservation(reservationUpdateDTO.getReservationId());
        Member member = getMember(reservationUpdateDTO.getMemberId());
        Camp camp = getCamp(reservationUpdateDTO.getCampId());

        if(!reservation.getMember().equals(member)) {
            log.error("예약을 한 회원과 일치하지 않음");
            throw new WrongMemberException("옳지 않은 회원");
        }
        if(!reservation.getCamp().equals(camp)) {
            log.error("캠핑장이 옳지 않음");
            throw new WrongMemberException("옳지 않은 캠피장");
        }

        dateConfirmation(camp, reservationUpdateDTO.getStartDate(), reservationUpdateDTO.getEndDate());

        reservation.modify(reservationUpdateDTO.getStartDate(), reservationUpdateDTO.getEndDate(), reservationUpdateDTO.getMoney());

        return reservation;
    }

    @Override
    public Long delete(ReservationDeleteDTO reservationDeleteDTO) {

        log.info("ReservationService delete 실행, ReservationDeleteDTO : {}", reservationDeleteDTO);

        Reservation reservation = getReservation(reservationDeleteDTO.getReservationId());
        Member member = getMember(reservationDeleteDTO.getMemberId());
        Camp camp = getCamp(reservationDeleteDTO.getCampId());

        if(!reservation.getMember().equals(member)) {
            throw new WrongMemberException("옳지 않은 회원");
        }
        if(!reservation.getCamp().equals(camp)) {
            throw new WrongMemberException("옳지 않는 캠핑장");
        }

        camp.reservationCancel();

        reservationRepository.delete(reservation);

        return reservation.getId();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> reservationList(Long memberId, Pageable pageable) {

        log.info("ReservationService reservationList 실행, memberId : {}", memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("잘못된 회원"));
        return reservationRepository.findByMember(member, pageable);
    }

    private Camp getCamp(Long campId) {
        return campRepository.findById(campId).orElseThrow(() -> new WrongIdException("존재하지 않는 캠핑장"));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new WrongIdException("존재하지 않는 예약"));
    }

    private void dateConfirmation(Camp camp, LocalDate startDate, LocalDate endDate) {
        if(startDate.isBefore(camp.getStartDate()) ||
                startDate.isAfter(endDate) ||
                endDate.isAfter(camp.getEndDate())) {
            throw new WrongDateException("잘못된 날짜");
        }
    }
}
