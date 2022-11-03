package kr.hs.dgsw.camfit.board.service;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.BoardDeleteDTO;
import kr.hs.dgsw.camfit.board.dto.BoardInsertDTO;
import kr.hs.dgsw.camfit.board.dto.BoardResponseDTO;
import kr.hs.dgsw.camfit.board.dto.BoardUpdateDTO;
import kr.hs.dgsw.camfit.board.repository.BoardRepository;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.exception.WrongMemberException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.repository.MemberRepository;
import kr.hs.dgsw.camfit.photo.Photo;
import kr.hs.dgsw.camfit.photo.repository.PhotoRepository;
import kr.hs.dgsw.camfit.photo.service.FileHandler;
import kr.hs.dgsw.camfit.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;
    private final PhotoRepository photoRepository;
    private final PhotoService photoService;

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Board insert(BoardInsertDTO boardInsertDTO, List<MultipartFile> files) throws IOException {

        log.info("boardService insert 실행, boardInsertDTO : {}, files : {}", boardInsertDTO, files);

        Member member = getMember(boardInsertDTO.getMemberId());

        Board board = Board.builder()
                .title(boardInsertDTO.getTitle())
                .content(boardInsertDTO.getContent())
                .member(member)
                .build();

        List<Photo> photos = fileHandler.parseFileInfo(board, files);

        // 파일이 존재할 때만 처리
        if(!photos.isEmpty()) {
            for (Photo photo : photos) {
                // 파일을 DB에 저장
                board.addPhoto(photoRepository.save(photo));
            }
        }

        boardRepository.save(board);

        return board;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Board update(BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) throws IOException {

        log.info("boardService update 실행, boardUpdateDTO : {}, files : {}", boardUpdateDTO, files);

        Member member = getMember(boardUpdateDTO.getMemberId());
        Board board = getBoard(boardUpdateDTO.getBoardId());

        if(!board.getMember().equals(member)) {
            throw new WrongMemberException("옳지 않은 회원");
        }

        List<Photo> photos = fileHandler.parseFileInfo(board, files);

        if(!photos.isEmpty()) {
            for (Photo photo : photos) {
                photoRepository.save(photo);
            }
        }

        board.modify(boardUpdateDTO.getTitle(), boardUpdateDTO.getContent());

        return board;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Long delete(BoardDeleteDTO boardDeleteDTO) {

        log.info("boardService delete 실행, boardDeleteDTO : {}", boardDeleteDTO);

        Member member = getMember(boardDeleteDTO.getMemberId());
        Board board = getBoard(boardDeleteDTO.getBoardId());

        if(!board.getMember().equals(member)) {
            throw new WrongMemberException("옳지 않은 회원");
        }

        boardRepository.delete(board);

        return board.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> list(String content, Pageable pageable) {

        log.info("boardService list 실행, 검색 : {}", content);

        return boardRepository.findByTitleContainingOrContentContaining(content, content, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> memberList(String username, Pageable pageable) {

        log.info("boardService memberList 실행, 유저 이름 : {}", username);

        Member member = memberRepository.findByUsername(username);

        return boardRepository.findByMember(member, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDTO searchById(Long id) {

        log.info("boardService searchById 실행, 게시판아이디 : {}", id);

        Board board = getBoard(id);

        return BoardResponseDTO.builder()
                .board(board)
                .path(photoService.findByIdList(id))
                .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new WrongIdException("존재하지 않는 글"));
    }
}
