package kr.hs.dgsw.camfit.member.service;

import kr.hs.dgsw.camfit.board.dto.BoardInsertDTO;
import kr.hs.dgsw.camfit.exception.DuplicateNameException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.dto.MemberUpdateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired MemberService memberService;

    private final String username = "seojun";
    private final String updateUsername = "kim";

    @Test
    @Rollback(false)
    public void 회원가입() {
        //given
        MemberInsertDTO memberInsertDTO = createInsertMember(username);

        //when
        Member member = memberService.insert(memberInsertDTO);
        System.out.println(member);
    }

    @Test
    //이미 존재하는 이름을 넘긴 경우
    public void 회원가입_오류1() {
        //given
        MemberInsertDTO memberInsertDTO = createInsertMember(username);

        //when
        memberService.insert(memberInsertDTO);

        //then
        assertThrows(DuplicateNameException.class, () -> {
            memberService.insert(memberInsertDTO);
        });

    }

    @Test
    //전과 다른 이름으로 변경하는 수정
    public void 회원_수정1() {
        //given
        Member member = createMember(username);
        MemberUpdateDTO memberUpdateDTO = createUpdateMember(updateUsername, member.getId());

        //when
        Member updateMember = memberService.update(memberUpdateDTO);

        //then
        assertThat(updateMember.getUsername()).isEqualTo(updateUsername);
        assertThat(updateMember.getPassword()).isEqualTo("12345");
        assertThat(updateMember.getGender()).isEqualTo("여");

    }

    @Test
    //전과 같은 이름으로 회원을 수정
    public void 회원_수정2() {
        //given
        Member member = createMember(username);
        MemberUpdateDTO memberUpdateDTO = createUpdateMember(username, member.getId());

        //when
        Member updateMember = memberService.update(memberUpdateDTO);

        //then
        assertThat(updateMember.getUsername()).isEqualTo(username);
        assertThat(updateMember.getPassword()).isEqualTo("12345");
        assertThat(updateMember.getGender()).isEqualTo("여");

    }

    @Test
    //이미 존재하는 이름으로 수정한 경우
    public void 회원_수정_오류1() {
        //given
        Member member1 = createMember(username + "1");
        Member member2 = createMember(username + "2");
        MemberUpdateDTO updateMember = createUpdateMember(username + "1", member2.getId());

        //then
        assertThrows(DuplicateNameException.class, () -> {
            memberService.update(updateMember);
        });
        assertThat(member2.getUsername()).isEqualTo(username + "2");

    }

    @Test
    //잘못된 아이디를 전송한 경우
    public void 회원_수정_오류2() {
        //given
        MemberUpdateDTO memberUpdateDTO = createUpdateMember(username, 1L);

        //then
        assertThrows(WrongIdException.class, () -> {
            memberService.update(memberUpdateDTO);
        });

    }

    @Test
    public void 회원_삭제() {
        //given
        Member member = createMember(username);

        //when
        Long memberId = memberService.delete(member.getId());

        //then
        assertThat(member.getId()).isEqualTo(memberId);

    }

    @Test
    //잘못된 아이디를 전송한 경우
    public void 회원_삭제_오류1() {
        //then
        assertThrows(WrongIdException.class, () -> {
            memberService.delete(1L);
        });

    }

    private MemberInsertDTO createInsertMember(String username) {

        System.out.println(memberService);

        return MemberInsertDTO.builder()
                .username(username)
                .password("1234")
                .gender("남")
                .dateOfBirth("051208")
                .phoneNumber("01089883538")
                .build();
    }

    private MemberUpdateDTO createUpdateMember(String username, Long memberId) {
        return MemberUpdateDTO.builder()
                .username(username)
                .password("12345")
                .gender("여")
                .dateOfBirth("071019")
                .phoneNumber("01012341234")
                .memberId(memberId)
                .build();
    }

    private Member createMember(String username) {
        MemberInsertDTO memberInsertDTO = createInsertMember(username);
        return memberService.insert(memberInsertDTO);
    }

}