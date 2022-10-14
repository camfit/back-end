package kr.hs.dgsw.camfit.member.controller;

import kr.hs.dgsw.camfit.jwt.JwtFilter;
import kr.hs.dgsw.camfit.jwt.TokenProvider;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.dto.*;
import kr.hs.dgsw.camfit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid MemberLoginDTO memberLoginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDTO.getUsername(), memberLoginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        log.info(memberLoginDTO.getUsername() + ", 이름의 멤버 로그인");

        return new ResponseEntity(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid MemberInsertDTO memberInsertDTO) {

        memberService.insert(memberInsertDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity modify(@RequestBody @Valid MemberUpdateDTO memberUpdateDTO) {

        Member member = memberService.update(memberUpdateDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity delete(@RequestParam Long memberId) {

        memberService.delete(memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/my/{username}")
    public ResponseEntity<MyMemberInformationDTO> searchByUsername(@PathVariable("username") String username) {

        Member member = memberService.findByUsername(username);

        MyMemberInformationDTO myMemberInformationDTO = MyMemberInformationDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .regdate(member.getRegdate())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .build();

        return ResponseEntity.ok().body(myMemberInformationDTO);
    }

    @GetMapping("/{username}")
    public ResponseEntity<MemberInformationDTO> myInformation(@PathVariable("username") String username) {

        Member member = memberService.findByUsername(username);

        MemberInformationDTO memberInformationDTO = MemberInformationDTO.builder()
                .username(member.getUsername())
                .regdate(member.getRegdate())
                .gender(member.getGender())
                .dateOfBirth(member.getDateOfBirth())
                .build();

        return ResponseEntity.ok().body(memberInformationDTO);
    }
}
