package kr.hs.dgsw.camfit.member;

import kr.hs.dgsw.camfit.authority.Authority;
import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.reservation.Reservation;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private LocalDate regdate;
    private String gender;
    private String dateOfBirth;
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

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

   /* @Builder
    public Member(String username, String password, String gender, String dateOfBirth, String phoneNumber, Authority authority) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.regdate = LocalDate.now();
        this.authorities
    }*/

    public void update(String username, String password, String gender, String dateOfBirth, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

}
