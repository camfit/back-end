package kr.hs.dgsw.camfit.member.service;

import kr.hs.dgsw.camfit.authority.Authority;
import kr.hs.dgsw.camfit.authority.util.SecurityUtil;
import kr.hs.dgsw.camfit.exception.DuplicateNameException;
import kr.hs.dgsw.camfit.exception.NotFoundMemberException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.dto.MemberUpdateDTO;
import kr.hs.dgsw.camfit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member insert(MemberInsertDTO memberInsertDTO) {

        if(memberRepository.existsByUsername(memberInsertDTO.getUsername())) {
            throw new DuplicateNameException("이미 존재하는 이름");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .username(memberInsertDTO.getUsername())
                .password(memberInsertDTO.getPassword())
                .gender(memberInsertDTO.getGender())
                .dateOfBirth(memberInsertDTO.getDateOfBirth())
                .phoneNumber(memberInsertDTO.getPhoneNumber())
                .authorities(Collections.singleton(authority))
                .build();

        return memberRepository.save(member);
    }

    @Override
    public Member update(MemberUpdateDTO memberUpdateDTO) {

        Member member = memberRepository.findById(memberUpdateDTO.getMemberId()).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));

        if(memberRepository.existsByUsername(memberUpdateDTO.getUsername()) &&
            !member.getUsername().equals(memberUpdateDTO.getUsername())) {
            throw new DuplicateNameException("이미 존재하는 이름");
        }

        member.update(memberUpdateDTO.getUsername(), memberUpdateDTO.getPassword(), memberUpdateDTO.getGender(), memberUpdateDTO.getDateOfBirth(), memberUpdateDTO.getPhoneNumber());

        return member;
    }

    @Override
    public Long delete(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));
        memberRepository.delete(member);

        return member.getId();
    }

    @Transactional(readOnly = true)
    public MemberInsertDTO getUserWithAuthorities(String username) {
        return MemberInsertDTO.from(memberRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberInsertDTO getMyUserWithAuthorities() {
        return MemberInsertDTO.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
