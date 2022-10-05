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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;
    private final PhotoRepository photoRepository;

    @Override
    public Board insert(BoardInsertDTO boardInsertDTO, List<MultipartFile> files) throws IOException {

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
    public Board update(BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) throws IOException {

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
    public Long delete(BoardDeleteDTO boardDeleteDTO) {

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
        return boardRepository.findByTitleContainingOrContentContaining(content, content, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> memberList(String username, Pageable pageable) {

        Member member = memberRepository.findByUsername(username);

        if(member == null) {
            throw new WrongIdException("잘못된 회원");
        }

        return boardRepository.findByMember(member, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDTO searchById(Long id, List<Long> fileId) {

        Board board = getBoard(id);

        return BoardResponseDTO.builder()
                .board(board)
                .fileId(fileId)
                .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new WrongIdException("존재하지 않는 글"));
    }
}
