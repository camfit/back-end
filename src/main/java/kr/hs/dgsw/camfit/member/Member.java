package kr.hs.dgsw.camfit.member;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private LocalDate regdate;
    private String role;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;

    @OneToMany(
            mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Board> boards = new ArrayList<>();

    @OneToMany(
            mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    @Setter
    @OneToOne(
            mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private Camp camp;

    @Builder
    public Member(String username, String password, String gender, String dateOfBirth, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.regdate = LocalDate.now();
        this.role = "ROLE_USER";
    }

    public void update(String username, String password, String gender, String dateOfBirth, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

}
