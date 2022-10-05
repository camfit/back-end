package kr.hs.dgsw.camfit.camp.service;

import kr.hs.dgsw.camfit.camp.Area;
import kr.hs.dgsw.camfit.camp.Camp;
import kr.hs.dgsw.camfit.camp.dto.CampDeleteDTO;
import kr.hs.dgsw.camfit.camp.dto.CampInsertDTO;
import kr.hs.dgsw.camfit.camp.dto.CampUpdateDTO;
import kr.hs.dgsw.camfit.camp.repository.CampRepository;
import kr.hs.dgsw.camfit.exception.WrongDateException;
import kr.hs.dgsw.camfit.exception.WrongIdException;
import kr.hs.dgsw.camfit.exception.WrongMemberException;
import kr.hs.dgsw.camfit.member.Member;
import kr.hs.dgsw.camfit.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CampServiceImpl implements CampService {

    private final CampRepository campRepository;
    private final MemberRepository memberRepository;

    @Override
    public Camp insert(CampInsertDTO campInsertDTO) {

        LocalDate startDate = LocalDate.parse(campInsertDTO.getStartDate());
        LocalDate endDate = LocalDate.parse(campInsertDTO.getEndDate());

        startDateAfterEndDateError(startDate, endDate);
        Member member = getMember(campInsertDTO.getMemberId());

        Area area = Area.valueOf(campInsertDTO.getArea());

        Camp camp = Camp.builder()
                .name(campInsertDTO.getName())
                .place(campInsertDTO.getPlace())
                .money(campInsertDTO.getMoney())
                .explanation(campInsertDTO.getExplanation())
                .phoneNumber(campInsertDTO.getPhoneNumber())
                .roomCount(campInsertDTO.getRoomCount())
                .area(area)
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .build();

        campRepository.save(camp);

        return camp;
    }

    @Override
    public Camp update(CampUpdateDTO campUpdateDTO) {

        LocalDate startDate = LocalDate.parse(campUpdateDTO.getStartDate());
        LocalDate endDate = LocalDate.parse(campUpdateDTO.getEndDate());

        Area area = Area.valueOf(campUpdateDTO.getArea());

        Camp camp = getCamp(campUpdateDTO.getCampId());
        Member member = getMember(campUpdateDTO.getMemberId());

        if(!member.getCamp().equals(camp)) {
            throw new WrongMemberException("옳지 않은 회원");
        }

        startDateAfterEndDateError(startDate, endDate);

        camp.modify(campUpdateDTO.getName(), campUpdateDTO.getPlace(), campUpdateDTO.getMoney(), campUpdateDTO.getExplanation(), camp.getPhoneNumber(), campUpdateDTO.getRoomCount(), area, startDate, endDate);

        return camp;
    }

    @Override
    public Long delete(CampDeleteDTO campDeleteDTO) {

        Camp camp = getCamp(campDeleteDTO.getCampId());
        Member member = getMember(campDeleteDTO.getMemberId());

        if(!member.getCamp().equals(camp)) {
            throw new WrongMemberException("옳지 않은 회원");
        }

        campRepository.delete(camp);

        return camp.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Camp> list(String name, Pageable pageable) {
        return campRepository.findByNameContaining(name, pageable);
    }

    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new WrongIdException("존재하지 않는 회원"));
        return member;
    }

    private Camp getCamp(Long campId) {
        Camp camp = campRepository.findById(campId).orElseThrow(() -> new WrongIdException("존재하지 않는 캠핑장"));
        return camp;
    }

    private void startDateAfterEndDateError(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new WrongDateException("잘못된 날짜");
        }
    }
}
