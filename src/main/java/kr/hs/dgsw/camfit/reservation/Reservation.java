package kr.hs.dgsw.camfit.reservation;

import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private int money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @Builder
    public Reservation(LocalDate startDate, LocalDate endDate, int money, Member member, Camp camp) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.money = money;

        this.member = member;
        member.getReservations().add(this);

        this.camp = camp;
        camp.getReservations().add(this);
    }

    public void modify(LocalDate startDate, LocalDate endDate, int money) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.money = money;
    }
}
