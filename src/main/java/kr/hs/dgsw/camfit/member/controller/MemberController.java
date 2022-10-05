package kr.hs.dgsw.camfit.member.controller;

import kr.hs.dgsw.camfit.member.dto.MemberInsertDTO;
import kr.hs.dgsw.camfit.member.dto.MemberUpdateDTO;
import kr.hs.dgsw.camfit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
