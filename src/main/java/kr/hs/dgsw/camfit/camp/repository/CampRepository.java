package kr.hs.dgsw.camfit.camp.repository;

import kr.hs.dgsw.camfit.camp.Camp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampRepository extends JpaRepository<Camp, Long> {
    public List<Camp> findByNameContaining(String name, Pageable pageable);
}
