package kr.hs.dgsw.camfit.camp.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.dto.CampDeleteDTO;
import kr.hs.dgsw.camfit.camp.dto.CampInsertDTO;
import kr.hs.dgsw.camfit.camp.dto.CampListDTO;
import kr.hs.dgsw.camfit.camp.dto.CampUpdateDTO;
import kr.hs.dgsw.camfit.camp.service.CampService;
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
@RequestMapping("/camp")
@RequiredArgsConstructor
public class CampController {

    private final CampService campService;

    @PostMapping("/registration")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity registration(@RequestBody @Valid CampInsertDTO campInsertDTO) {

        campService.insert(campInsertDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity modify(@RequestBody @Valid CampUpdateDTO campUpdateDTO) {

        campService.update(campUpdateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity delete(@RequestParam(value = "member_id") Long memberId,
                       @RequestParam(value = "camp_id") Long campId) {

        CampDeleteDTO campDeleteDTO = CampDeleteDTO.builder()
                .memberId(memberId)
                .campId(campId)
                .build();

        campService.delete(campDeleteDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CampListDTO>> campList(
            @RequestParam(value = "name", defaultValue = "") String name,

            @Parameter(hidden = true)
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        List<Camp> campList = campService.list(name, pageable);
        List<CampListDTO> campListDTOList = new ArrayList<>();

        for (Camp camp : campList) {
            campListDTOList.add(
                    CampListDTO.builder()
                            .id(camp.getId())
                            .name(camp.getName())
                            .place(camp.getPlace())
                            .money(camp.getMoney())
                            .explanation(camp.getExplanation())
                            .roomCount(camp.getRoomCount())
                            .area(camp.getArea())
                            .build()
            );
        }

        return ResponseEntity.ok().body(campListDTOList);
    }
}
