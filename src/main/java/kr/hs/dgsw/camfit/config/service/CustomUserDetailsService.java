package kr.hs.dgsw.camfit.config.service;

import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

   private final MemberRepository memberRepository;

   public CustomUserDetailsService(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      return memberRepository.findOneWithAuthoritiesByUsername(username)
         .map(member -> createUser(username, member))
         .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, Member member) {

      List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(member.getUsername(),
              member.getPassword(),
              grantedAuthorities);
   }
}
