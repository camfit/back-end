package kr.hs.dgsw.camfit.board.service;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.BoardDeleteDTO;
import kr.hs.dgsw.camfit.board.dto.BoardInsertDTO;
import kr.hs.dgsw.camfit.board.dto.BoardUpdateDTO;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.exception.WrongMemberException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardServiceImplTest {

    @Autowired BoardService boardService;
    @Autowired MemberService memberService;

    private final String username = "seojun";
    private final String title = "test title";
    private final String content = "test content";

    @Test
    @WithMockUser(roles = "USER")
    public void 글_작성() throws IOException {
        //given
        Member member = createMember(username);
        BoardInsertDTO boardInsertDTO = createBoardInsertDTO(member.getId());

        //when
        Board board = boardService.insert(boardInsertDTO, null);

        //then
        assertThat(board.getTitle()).isEqualTo(title);

    }

    @Test
    //잘못된 회원 아이디를 보낸 경우
    public void 글_작성_오류1() {
        //given
        BoardInsertDTO boardInsertDTO = createBoardInsertDTO(1L);

        //then
        assertThrows(WrongIdException.class, () -> {
            boardService.insert(boardInsertDTO, null);
        });

    }

    @Test
    //제목과 내용 모두 수정
    public void 글_수정1() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member.getId(), board.getId(), "update title", "update content");

        //when
        Board updateBoard = boardService.update(boardUpdateDTO, null);

        //then
        assertThat(board.getTitle()).isEqualTo(updateBoard.getTitle());
        assertThat(board.getContent()).isEqualTo(updateBoard.getContent());

    }

    @Test
    //제목만 수정
    public void 글_수정2() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member.getId(), board.getId(), "update title", content);

        //when
        Board updateBoard = boardService.update(boardUpdateDTO, null);

        //then
        assertThat(board.getTitle()).isEqualTo(boardUpdateDTO.getTitle());
        assertThat(board.getContent()).isEqualTo(boardUpdateDTO.getContent());

    }

    @Test
    //내용만 수정
    public void 글_수정3() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member.getId(), board.getId(), title, "update content");

        //when
        Board updateBoard = boardService.update(boardUpdateDTO, null);

        //then
        assertThat(board.getTitle()).isEqualTo(boardUpdateDTO.getTitle());
        assertThat(board.getContent()).isEqualTo(boardUpdateDTO.getContent());

    }

    @Test
    //잘못된 회원 아이디를 보낸 경우
    public void 글_수정_오류1() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member.getId() + 1, board.getId(), title, content);

        //then
        assertThrows(WrongIdException.class, () -> {
            boardService.update(boardUpdateDTO, null);
        });

    }

    @Test
    //잘못된 글 아이디를 보낸 경우
    public void 글_수정_오류2() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member.getId(), board.getId() + 1, title, content);

        //then
        assertThrows(WrongIdException.class, () -> {
            boardService.update(boardUpdateDTO, null);
        });

    }

    @Test
    //게시글을 올린 회원이 아닌 다른 회원을 보낸 경우
    public void 글_수정_오류3() throws IOException {
        //given
        Member member1 = createMember(username + "1");
        Member member2 = createMember(username + "2");
        Board board = createBoard(member1.getId());
        BoardUpdateDTO boardUpdateDTO = createBoardUpdateDTO(member2.getId(), board.getId(), title, content);

        //then
        assertThrows(WrongMemberException.class, () -> {
            boardService.update(boardUpdateDTO, null);
        });

    }

    @Test
    public void 글_삭제() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardDeleteDTO boardDeleteDTO = createBoardDeleteDTO(member.getId(), board.getId());

        //when
        Long boardId = boardService.delete(boardDeleteDTO);

        //then
        assertThat(board.getId()).isEqualTo(boardId);

    }

    @Test
    //잘못된 회원을 보낸 경우
    public void 글_삭제_오류1() throws IOException {
        //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardDeleteDTO boardDeleteDTO = createBoardDeleteDTO(member.getId() + 1, board.getId());

        //then
        assertThrows(WrongIdException.class, () -> {
            boardService.delete(boardDeleteDTO);
        });

    }

    @Test
    //잘못된 글을 보낸 경우
    public void 글_삭제_오류2() throws IOException {
       //given
        Member member = createMember(username);
        Board board = createBoard(member.getId());
        BoardDeleteDTO boardDeleteDTO = createBoardDeleteDTO(member.getId(), board.getId() + 1);

        //then
        assertThrows(WrongIdException.class, () -> {
            boardService.delete(boardDeleteDTO);
        });

    }

    @Test
    //회원 아이디가 글을 쓴 아이디가 아닌 경우
    public void 글_삭제_오류3() throws IOException {
        //given
        Member member1 = createMember(username + "1");
        Member member2 = createMember(username + "2");
        Board board = createBoard(member1.getId());
        BoardDeleteDTO boardDeleteDTO = createBoardDeleteDTO(member2.getId(), board.getId());

        //then
        assertThrows(WrongMemberException.class, () -> {
            boardService.delete(boardDeleteDTO);
        });

    }

    private MemberInsertDTO createInsertMember(String username) {
        return MemberInsertDTO.builder()
                .username(username)
                .password("1234")
                .gender("남")
                .dateOfBirth("051208")
                .phoneNumber("01089883538")
                .build();
    }

    private Member createMember(String username) {
        MemberInsertDTO memberInsertDTO = createInsertMember(username);
        return memberService.insert(memberInsertDTO);
    }

    private BoardInsertDTO createBoardInsertDTO(Long memberId) {
        return BoardInsertDTO.builder()
                .title(title)
                .content(content)
                .memberId(memberId)
                .build();
    }

    private BoardUpdateDTO createBoardUpdateDTO(Long memberId, Long boardId, String title, String content) {
        return BoardUpdateDTO.builder()
                .title(title)
                .content(content)
                .memberId(memberId)
                .boardId(boardId)
                .build();
    }

    private BoardDeleteDTO createBoardDeleteDTO(Long memberId, Long boardId) {
        return BoardDeleteDTO.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();
    }

    private Board createBoard(Long memberId) throws IOException {
        BoardInsertDTO boardInsertDTO = createBoardInsertDTO(memberId);
        return boardService.insert(boardInsertDTO, null);
    }

}