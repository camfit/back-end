package kr.hs.dgsw.camfit.reservation.service;

import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.dto.CampInsertDTO;
import kr.hs.dgsw.camfit.camp.repository.CampRepository;
import kr.hs.dgsw.camfit.camp.service.CampService;
import kr.hs.dgsw.camfit.exception.DuplicateNameException;
import kr.hs.dgsw.camfit.exception.WrongDateException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.service.MemberService;
import kr.hs.dgsw.camfit.reservation.Reservation;
import kr.hs.dgsw.camfit.reservation.dto.ReservationInsertDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReservationServiceImplTest {

    @Autowired ReservationService reservationService;
    @Autowired MemberService memberService;
    @Autowired CampService campService;
    @Autowired CampRepository campRepository;

    private final String username = "seojun";

    @Test
    @Rollback(false)
    public void 캠핑장_예약() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(member.getId());
        ReservationInsertDTO reservationInsertDTO = createReservationInsertDTO(1, 3, member.getId(), camp.getId());

        //when
        Reservation reservation = reservationService.insert(reservationInsertDTO);

        //then
        assertThat(reservation.getMember()).isEqualTo(member);
        assertThat(reservation.getCamp()).isEqualTo(camp);

    }

    @Test
    //예약 시작날이 캠핑 시작날보다 빨리 예약한 경우
    public void 캠핑장_예약_오류1() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(member.getId());

        //then
        assertThrows(WrongDateException.class, () -> createReservation(-5, 2, member.getId(), camp.getId()));

    }

    @Test
    //예약 시작날이 예약 마지막날보다 더 늦는 경우
    public void 캠핑장_예약_오류2() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(member.getId());

        //then
        assertThrows(WrongDateException.class, () -> createReservation(2, 1, member.getId(), camp.getId()));

    }

    @Test
    //예약 마지막날이 캠핑 마지막날보다 늦는 경우
    public void 캠핑장_에약_오류3() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(member.getId());

        //then
        assertThrows(WrongDateException.class, () -> createReservation(2, 6, member.getId(), camp.getId()));

    }


    private ReservationInsertDTO createReservationInsertDTO(int startDate, int endDate, Long memberId, Long campId) {

        Camp camp = campRepository.findById(campId).orElseThrow(() -> new WrongIdException("잘못된 아이디"));

        return ReservationInsertDTO.builder()
                .startDate(LocalDate.now().plusDays(startDate).toString())
                .endDate(LocalDate.now().plusDays(endDate).toString())
                .money(camp.getMoney() * endDate)
                .memberId(memberId)
                .campId(campId)
                .build();
    }

    private Reservation createReservation(int startDate, int endDate, Long memberId, Long campId) {

        ReservationInsertDTO reservationInsertDTO = createReservationInsertDTO(startDate, endDate, memberId, campId);
        return reservationService.insert(reservationInsertDTO);
    }

    private Member createMember(String username) {

        MemberInsertDTO memberInsertDTO = MemberInsertDTO.builder()
                .username(username)
                .password("1234")
                .build();

        return memberService.insert(memberInsertDTO);
    }

    private Camp createCamp(Long memberId) {

        CampInsertDTO campInsertDTO = CampInsertDTO.builder()
                .name("test name")
                .place("테크노 하나리움")
                .money(50000)
                .explanation("재미있다.")
                .phoneNumber("010-8988-3538")
                .roomCount(3)
                .area("경기도")
                .startDate(LocalDate.now().minusDays(2).toString())
                .endDate(LocalDate.now().plusDays(4).toString())
                .memberId(memberId)
                .build();

        return campService.insert(campInsertDTO);
    }

}