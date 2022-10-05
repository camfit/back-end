package kr.hs.dgsw.camfit.photo.repository;

import kr.hs.dgsw.camfit.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByBoardId(Long boardId);
}
