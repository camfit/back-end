package kr.hs.dgsw.camfit.reservation.controller;

import kr.hs.dgsw.camfit.camp.dto.CampDeleteDTO;
import kr.hs.dgsw.camfit.reservation.Reservation;
import kr.hs.dgsw.camfit.reservation.dto.ReservationDeleteDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationInsertDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationListDTO;
import kr.hs.dgsw.camfit.reservation.dto.ReservationUpdateDTO;
import kr.hs.dgsw.camfit.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservation")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity reservation(@RequestBody @Valid ReservationInsertDTO reservationInsertDTO) {

        reservationService.insert(reservationInsertDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity modify(@RequestBody @Valid ReservationUpdateDTO reservationUpdateDTO) {

        reservationService.update(reservationUpdateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/cancel")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity cancel(@RequestParam(value = "member_id") Long memberId,
                       @RequestParam(value = "camp_id") Long campId,
                       @RequestParam(value = "reservation_id") Long reservationId) {

        ReservationDeleteDTO reservationDeleteDTO = ReservationDeleteDTO.builder()
                .memberId(memberId)
                .campId(campId)
                .reservationId(reservationId)
                .build();

        reservationService.delete(reservationDeleteDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ReservationListDTO>> reservationList(@RequestParam(value = "member_id") Long memberId,
                                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        List<Reservation> reservationList = reservationService.reservationList(memberId, pageable);
        List<ReservationListDTO> reservationListDTOList = new ArrayList<>();

        for (Reservation reservation : reservationList) {
            reservationListDTOList.add(
                    ReservationListDTO.builder()
                            .id(reservation.getId())
                            .startDate(reservation.getStartDate())
                            .endDate(reservation.getEndDate())
                            .money(reservation.getMoney())
                            .campName(reservation.getCamp().getName())
                            .build()
            );
        }

        return ResponseEntity.ok().body(reservationListDTOList);
    }
}
