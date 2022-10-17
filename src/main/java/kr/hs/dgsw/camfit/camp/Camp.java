package kr.hs.dgsw.camfit.camp;

import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.reservation.Reservation;
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
@Table(name = "camp")
public class Camp {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "place", nullable = false)
    private String place;

    @Column(name = "money", nullable = false)
    private int money;

    @Column(name = "explanation", nullable = false)
    private String explanation;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "room_count", nullable = false)
    private Short roomCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    private Area area;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "camp")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Camp(String name, String place, int money, String explanation, String phoneNumber, Short roomCount, Area area, LocalDate startDate, LocalDate endDate, Member member) {
        this.name = name;
        this.place = place;
        this.money = money;
        this.explanation = explanation;
        this.phoneNumber = phoneNumber;
        this.roomCount = roomCount;
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
        member.setCamp(this);
    }

    public void modify(String name, String place, int money, String explanation, String phoneNumber, Short roomCount, Area area, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.place = place;
        this.money = money;
        this.explanation = explanation;
        this.phoneNumber = phoneNumber;
        this.roomCount = roomCount;
        this.area = area;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void reservationCamp() {
        this.roomCount = (short)(this.roomCount - 1);
    }

    public void reservationCancel() {
        this.roomCount = (short)(this.roomCount + 1);
    }
}
