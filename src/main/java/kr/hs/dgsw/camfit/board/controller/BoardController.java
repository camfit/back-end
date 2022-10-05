package kr.hs.dgsw.camfit.board.controller;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.*;
import kr.hs.dgsw.camfit.board.service.BoardService;
import kr.hs.dgsw.camfit.photo.dto.PhotoResponseDTO;
import kr.hs.dgsw.camfit.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PhotoService photoService;

    @PostMapping("/post")
    public void post(@RequestBody @Valid BoardInsertDTO boardInsertDTO, List<MultipartFile> files) throws IOException {
        boardService.insert(boardInsertDTO, files);
    }

    @PutMapping("/modify")
    public void modify(@RequestBody @Valid BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) throws IOException {
        boardService.update(boardUpdateDTO, files);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam(value = "member_id") Long memberId,
                       @RequestParam(value = "board_id") Long boardId) {

        BoardDeleteDTO boardDeleteDTO = BoardDeleteDTO.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();

        boardService.delete(boardDeleteDTO);
    }

    @GetMapping("/")
    public List<BoardListDTO> boardList(@RequestParam(defaultValue = "") String content,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 3) Pageable pageable) {

        List<Board> boardList = boardService.list(content, pageable);
        return getBoardListDTOList(boardList);
    }

    @GetMapping("/{username}")
    public List<BoardListDTO> memberBoardList(@PathVariable("username") String username,
                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 3) Pageable pageable) {

        List<Board> boardList = boardService.memberList(username, pageable);
        return getBoardListDTOList(boardList);
    }

    /**
     * 개별 조회
     */
    @GetMapping("/{id}")
    public BoardResponseDTO searchById(@PathVariable("id") Long id) {

        // 게시글 id로 해당 게시글 첨부파일 전체 조회
        List<PhotoResponseDTO> photoResponseDTOList = photoService.findAllByBoard(id);
        // 게시글 첨부파일 id를 담을 List 객체 생성
        List<Long> photoId = new ArrayList<>();

        // 각 첨부파일 id 추가
        for (PhotoResponseDTO photoResponseDTO : photoResponseDTOList) {
            photoId.add(photoResponseDTO.getFileId());
        }

        // 게시글 id와 첨부파일 id 목록 전달받아 결과 반환
        return boardService.searchById(id, photoId);
    }

    private List<BoardListDTO> getBoardListDTOList(List<Board> boardList) {

        List<BoardListDTO> boardListDTOList = new ArrayList<>();

        for (Board board : boardList) {
            boardListDTOList.add(
                    BoardListDTO.builder()
                            .id(board.getId())
                            .title(board.getTitle())
                            .content(board.getContent())
                            .thumbnailId(!board.getPhotos().isEmpty() ? board.getPhotos().get(0).getId() : 0L) // 첨부파일이 존재한다면 첫 번째 사진을 썸네일로 사용 첨부파일이 없다면 기본 썸네일을 사용
                            .regdate(board.getModifyDate() == null ? board.getRegdate() : board.getModifyDate())
                            .username(board.getMember().getUsername())
                            .build()
            );
        }

        return boardListDTOList;
    }
}
