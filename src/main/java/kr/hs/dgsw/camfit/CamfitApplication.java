package kr.hs.dgsw.camfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

/**
 * 캠핑 예약날이 지나면 자동으로 예약 목록에서 없어지는 것
 * 시간이 남으면 캠핑장에 있는 방도 db로 만들어서 세부적인 방도 만들 것
 * camp 등록이나 수정할 때 날짜나 지역이 잘못된 것인지 확인하는 코드를 에러 컨트롤러에 만들 것 (enum 에러는 IllegalArgumentException, localDate 에러는 DateTimeParseException)
 * camp test 코드 더 정밀하게 할 것
 * list 출력할 때 DTO 사용과 entity에 Json 어노테이션 붙이기 참고(https://bellog.tistory.com/149) ☑ (Json 어노테이션 사용 안 함)
 * entity cascade 사용하기 ☑
 * 게시판 구현5 PhotoController 부터
 * transactional(readOnly=ture) 설정하기 ☑
 * jwt 사용 ☑
 * CrosConfig 고치기 ☑
 * swagger 설정 제대로 수정하기 ☑
 * boardLit에서 photoId가 아닌 photoPath 반환
 */
@SpringBootApplication
public class CamfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamfitApplication.class, args);
	}

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customize() {
		return p -> {
			p.setOneIndexedParameters(true); // page를 1부터 시작
			p.setMaxPageSize(10); // page의 사이즈를 10으로 저장
		};
	}

}
