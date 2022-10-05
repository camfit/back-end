package kr.hs.dgsw.camfit.photo;

import kr.hs.dgsw.camfit.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "photo")
public class Photo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    private String origFileName; // 파일 원본명
    private String filePath; // 파일 저장 경로
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Photo(String origFileName, String filePath, Long fileSize) {
        this.origFileName = origFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    // Board 정보 저장
    public void setBoard(Board board) {
        this.board = board;

        // 게시글에 현재 파일이 존재하지 않는다면
        if(!board.getPhotos().contains(this)) {
            // 파일 추가
            board.getPhotos().add(this);
        }
    }
}
