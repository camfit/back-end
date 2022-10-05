package kr.hs.dgsw.camfit.board;

import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.photo.Photo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "board")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;
    private LocalDate regdate;
    private LocalDate modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(
            mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.regdate = LocalDate.now();

        this.member = member;
        member.getBoards().add(this);
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifyDate = LocalDate.now();
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(photo.getBoard() !=  this) {
            // 파일 저장
            photo.setBoard(this);
        }
    }
}
