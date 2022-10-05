package kr.hs.dgsw.camfit.camp.service;

import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.dto.CampDeleteDTO;
import kr.hs.dgsw.camfit.camp.dto.CampInsertDTO;
import kr.hs.dgsw.camfit.camp.dto.CampUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampService {
    public Camp insert(CampInsertDTO campInsertDTO);
    public Camp update(CampUpdateDTO campUpdateDTO);
    public Long delete(CampDeleteDTO campDeleteDTO);

    List<Camp> list(String name, Pageable pageable);
}
