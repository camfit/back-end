package kr.hs.dgsw.camfit.camp.service;

import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.dto.CampDeleteDTO;
import kr.hs.dgsw.camfit.camp.dto.CampInsertDTO;
import kr.hs.dgsw.camfit.camp.dto.CampUpdateDTO;
import kr.hs.dgsw.camfit.exception.DuplicateNameException;
import kr.hs.dgsw.camfit.exception.WrongDateException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CampServiceImplTest {

    @Autowired CampService campService;
    @Autowired MemberService memberService;

    private final String username = "seojun";

    @Test
    public void 캠핑장_등록() {
        //given
        Member member = createMember(username);
        CampInsertDTO campInsertDTO = createCampInsertDTO(4, member.getId());

        //when
        Camp camp = campService.insert(campInsertDTO);

        //then
        assertThat(camp.getPlace()).isEqualTo(campInsertDTO.getPlace());

    }

    @Test
    //캠핑 시작 날짜가 마지막 날짜보다 늦는 경우 오류
    public void 캠핑장_등록_오류1() {
        //given
        Member member = createMember(username);
        CampInsertDTO campInsertDTO = createCampInsertDTO(-4, member.getId());

        //then
        assertThrows(WrongDateException.class, () -> campService.insert(campInsertDTO));

    }

    @Test
    //잘못된 회원을 넘긴 경우
    public void 캠핑장_등록_오류2() {
        //given
        Member member = createMember(username);
        CampInsertDTO campInsertDTO = createCampInsertDTO(3, member.getId() + 1);

        //then
        assertThrows(WrongIdException.class, () -> campService.insert(campInsertDTO));

    }

    @Test
    public void 캠핑장_수정() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(3, member.getId());
        CampUpdateDTO campUpdateDTO = createCampUpdateDTO(3, member.getId(), camp.getId());

        //when
        Camp updateCamp = campService.update(campUpdateDTO);

        //then
        assertThat(camp.getName()).isEqualTo(updateCamp.getName());

    }

    @Test
    //잘못된 캠핑장을 넘긴 경우
    public void 캠핑장_수정_오류1() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(3, member.getId());
        CampUpdateDTO campUpdateDTO = createCampUpdateDTO(3, member.getId(), camp.getId() + 1);

        //then
        assertThrows(WrongIdException.class, () -> campService.update(campUpdateDTO));

    }

    @Test
    //수정된 캠핑장의 날짜가 잘못된 경우
    public void 캠핑장_수정_오류2() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(3, member.getId());
        CampUpdateDTO campUpdateDTO = createCampUpdateDTO(-2, member.getId(), camp.getId());

        //then
        assertThrows(WrongDateException.class, () -> campService.update(campUpdateDTO));

    }

    @Test
    public void 캠핑장_삭제() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(3, member.getId());
        CampDeleteDTO campDeleteDTO = createCampDeleteDTO(member.getId(), camp.getId());

        //when
        Long campId = campService.delete(campDeleteDTO);

        //then
        assertThat(campId).isEqualTo(camp.getId());

    }

    @Test
    //잘못된 캠핑장을 넘긴 경우
    public void 캠핑장_삭제_오류1() {
        //given
        Member member = createMember(username);
        Camp camp = createCamp(3, member.getId());
        CampDeleteDTO campDeleteDTO = createCampDeleteDTO(member.getId(), camp.getId() + 1);

        //then
        assertThrows(WrongIdException.class, () -> campService.delete(campDeleteDTO));

    }

    private Member createMember(String username) {
        MemberInsertDTO memberInsertDTO = MemberInsertDTO.builder()
                .username(username)
                .password("1234")
                .gender("남")
                .phoneNumber("01012341234")
                .dateOfBirth("051208")
                .build();

        return memberService.insert(memberInsertDTO);
    }

    private CampInsertDTO createCampInsertDTO(int endDate, Long memberId) {
        return CampInsertDTO.builder()
                .name("test name")
                .place("테크노 하나리움")
                .money(50000)
                .explanation("재미있다.")
                .phoneNumber("010-8988-3538")
                .roomCount(3)
                .area("경기도")
                .startDate(LocalDate.now().toString())
                .endDate(LocalDate.now().plusDays(endDate).toString())
                .memberId(memberId)
                .build();
    }

    private CampUpdateDTO createCampUpdateDTO(int endDate, Long memberId, Long campId) {

        return CampUpdateDTO.builder()
                .name("update name")
                .place("테크노 하나리움")
                .money(50000)
                .explanation("수정된 재미있다.")
                .phoneNumber("010-8988-3538")
                .roomCount(3)
                .area("경기도")
                .startDate(LocalDate.now().toString())
                .endDate(LocalDate.now().plusDays(endDate).toString())
                .campId(campId)
                .memberId(memberId)
                .build();
    }

    private CampDeleteDTO createCampDeleteDTO(Long memberId, Long campId) {
        return CampDeleteDTO.builder()
                .memberId(memberId)
                .campId(campId)
                .build();
    }

    private Camp createCamp(int endDate, Long memberId) {
        CampInsertDTO campInsertDTO = createCampInsertDTO(endDate, memberId);
        return campService.insert(campInsertDTO);
    }

}