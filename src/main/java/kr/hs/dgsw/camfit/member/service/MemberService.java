package kr.hs.dgsw.camfit.member.service;

import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.dto.MemberUpdateDTO;

public interface MemberService {
    public Member insert(MemberInsertDTO memberInsertDTO);
    public Member update(MemberUpdateDTO memberUpdateDTO);
    public Long delete(Long memberId);
}
