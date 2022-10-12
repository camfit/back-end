package kr.hs.dgsw.camfit.member.controller;

import kr.hs.dgsw.camfit.jwt.JwtFilter;
import kr.hs.dgsw.camfit.jwt.TokenProvider;
import kr.hs.dgsw.camfit.member.dto.*;
import kr.hs.dgsw.camfit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<MemberJoinResponseDTO> login(@Valid @RequestBody MemberLoginDTO memberLoginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDTO.getUsername(), memberLoginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/join")
    public void join(@RequestBody @Valid MemberInsertDTO memberInsertDTO) {
        memberService.insert(memberInsertDTO);
    }

    @PutMapping("/modify")
    public void modify(@RequestBody @Valid MemberUpdateDTO memberUpdateDTO) {
        memberService.update(memberUpdateDTO);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long memberId) {
        memberService.delete(memberId);
    }
}
