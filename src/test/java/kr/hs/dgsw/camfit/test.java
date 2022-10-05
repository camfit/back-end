package kr.hs.dgsw.camfit;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.BoardDeleteDTO;
import kr.hs.dgsw.camfit.board.dto.BoardInsertDTO;
import kr.hs.dgsw.camfit.board.dto.BoardUpdateDTO;
import kr.hs.dgsw.camfit.board.service.BoardService;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.exception.WrongMemberException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class test {

    @Test
    public void localDate_오류() {
        //given
        LocalDate localDate = LocalDate.parse("2020-12-08");
        System.out.println(localDate);

        //when

        //then

    }
}