package kr.hs.dgsw.camfit.board.controller;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.board.dto.*;
import kr.hs.dgsw.camfit.board.service.BoardService;
import kr.hs.dgsw.camfit.photo.Photo;
import kr.hs.dgsw.camfit.photo.dto.PhotoResponseDTO;
import kr.hs.dgsw.camfit.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final PhotoService photoService;

    @PostMapping("/post")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity post(@RequestPart @Valid BoardInsertDTO boardInsertDTO, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        boardService.insert(boardInsertDTO, files);
        log.info(boardInsertDTO.getTitle() + ", 제목의 게시글 작성");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity modify(@RequestPart @Valid BoardUpdateDTO boardUpdateDTO, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        boardService.update(boardUpdateDTO, files);
        log.info(boardUpdateDTO.getTitle() + ", 제목의 게시글 수정");
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity delete(@RequestParam(value = "member_id") Long memberId,
                       @RequestParam(value = "board_id") Long boardId) {

        BoardDeleteDTO boardDeleteDTO = BoardDeleteDTO.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();

        boardService.delete(boardDeleteDTO);
        log.info(boardId + ", 아이디 게시글 삭제");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<BoardListDTO>> boardList(@RequestParam(defaultValue = "") String content,
                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 3) Pageable pageable) {

        List<Board> boardList = boardService.list(content, pageable);
        return ResponseEntity.ok().body(getBoardListDTOList(boardList));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<BoardListDTO>> memberBoardList(@PathVariable("username") String username,
                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 3) Pageable pageable) {

        List<Board> boardList = boardService.memberList(username, pageable);
        return ResponseEntity.ok(getBoardListDTOList(boardList));
    }

    /**
     * 개별 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDTO> searchById(@PathVariable("id") Long id) {

        // 게시글 id로 해당 게시글 첨부파일 전체 조회
        List<PhotoResponseDTO> photoResponseDTOList = photoService.findAllByBoard(id);
        // 게시글 첨부파일 id를 담을 List 객체 생성
        List<Long> photoId = new ArrayList<>();

        // 각 첨부파일 id 추가
        for (PhotoResponseDTO photoResponseDTO : photoResponseDTOList) {
            photoId.add(photoResponseDTO.getFileId());
        }

        // 게시글 id와 첨부파일 id 목록 전달받아 결과 반환
        return ResponseEntity.ok().body(boardService.searchById(id, photoId));
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
