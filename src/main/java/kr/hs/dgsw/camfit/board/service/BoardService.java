package kr.hs.dgsw.camfit.board.service;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.BoardDeleteDTO;
import kr.hs.dgsw.camfit.board.dto.BoardInsertDTO;
import kr.hs.dgsw.camfit.board.dto.BoardResponseDTO;
import kr.hs.dgsw.camfit.board.dto.BoardUpdateDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    public Board insert(BoardInsertDTO boardInsertDTO, List<MultipartFile> files) throws IOException;
    public Board update(BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) throws IOException;
    public Long delete(BoardDeleteDTO boardDeleteDTO);
    public List<Board> list(String content, Pageable pageable);
    public List<Board> memberList(String username, Pageable pageable);
    public BoardResponseDTO searchById(Long id);
}
