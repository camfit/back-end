package kr.hs.dgsw.camfit.member.repository;

import kr.hs.dgsw.camfit.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public boolean existsByUsername(String username);
    public Member findByUsername(String username);
}
