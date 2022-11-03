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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member insert(MemberInsertDTO memberInsertDTO) {

        log.info("memberService insert 실행, memberInsertDTO : {}", memberInsertDTO);

        if(memberRepository.existsByUsername(memberInsertDTO.getUsername())) {
            log.error("이미 존재하는 username : ", memberInsertDTO.getUsername());
            throw new DuplicateNameException("이미 존재하는 이름");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .username(memberInsertDTO.getUsername())
                .password(passwordEncoder.encode(memberInsertDTO.getPassword()))
                .gender(memberInsertDTO.getGender())
                .dateOfBirth(memberInsertDTO.getDateOfBirth())
                .phoneNumber(memberInsertDTO.getPhoneNumber())
                .authorities(Collections.singleton(authority))
                .regdate(LocalDate.now())
                .build();

        return memberRepository.save(member);
    }

    @Override
    public Member update(MemberUpdateDTO memberUpdateDTO) {

        log.info("memberService update 실행, memberUpdateDTO : {}", memberUpdateDTO);

        Member member = memberRepository.findById(memberUpdateDTO.getMemberId()).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));

        if(memberRepository.existsByUsername(memberUpdateDTO.getUsername()) &&
            !member.getUsername().equals(memberUpdateDTO.getUsername())) {
            log.error("이미 존재하는 username : ", memberUpdateDTO.getUsername());
            throw new DuplicateNameException("이미 존재하는 이름");
        }

        member.update(memberUpdateDTO.getUsername(), memberUpdateDTO.getPassword(), memberUpdateDTO.getGender(), memberUpdateDTO.getDateOfBirth(), memberUpdateDTO.getPhoneNumber());

        return member;
    }

    @Override
    public Long delete(Long memberId) {

        log.info("memberService delete 실행, memberId : {}", memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            log.error("존재하지 않는 회원아이디 : {}", memberId);
            throw new WrongIdException("존재하지 않는 회원");
        });
        memberRepository.delete(member);

        return member.getId();
    }

    @Override
    public Member findByUsername(String username) {

        log.info("memberService findByUsername 실행, username : {}", username);

        return memberRepository.findByUsername(username);
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
