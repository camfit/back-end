package kr.hs.dgsw.camfit.board.repository;

import kr.hs.dgsw.camfit.board.Board;
import kr.hs.dgsw.camfit.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    public List<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    public List<Board> findByMember(Member member, Pageable pageable);
}
