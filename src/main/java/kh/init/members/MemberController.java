package kh.init.members;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

@RequestMapping("/member")
@Controller
public class MemberController {

	@Autowired
	private MemberService service;
	@Autowired
	private HttpSession session;

	// 로그인	유효성 검사
	@RequestMapping("/loginProc.do")
	public String toLogin(String email, String pw) {
		if(email != null && pw != null) {
			System.out.println("로그인 시도 : " + email);
		}
		if(service.isLoginOk(email, pw) > 0) { // 로그인 허가
			session.setAttribute("loginInfo", service.getMemberDTO(email)); // 세션 로그인정보 담기
			//로그인 세션 테스트 코드 시작
			String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
			System.out.println("로그인 세션 값 확인 : " + email1);
			//로그인 세션 테스트 코드 끝
			return "redirect:/feed/getFriendFeed";
		}else {
			return "main";
		}
	}
	
	// 로그인	유효성 검사
	@RequestMapping("/kakaoLoginProc")
	public String toKaKaoLogin(@RequestParam("code") String code) {
		System.out.println("code : " + code);
		String authorized_code = service.getAccessToken(code);
		System.out.println("authorized_code : " + authorized_code);
//		if(email != null && pw != null) {
//			System.out.println("로그인 시도 : " + email);
//		}
//		if(service.isLoginOk(email, pw) > 0) { // 로그인 허가
//			session.setAttribute("loginInfo", service.getMemberDTO(email)); // 세션 로그인정보 담기
//			return "redirect:/feed/getFriendFeed";
//		}else {
//			return "main";
//		}
		return "main";
	}

	// 로그아웃 세션 삭제
	@RequestMapping("/logout.do")
	public String toLogout() {
		//로그인 세션 테스트 코드 시작
		String email = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email);
		//로그인 세션 테스트 코드 끝
		System.out.println("로그아웃 > " + session.getAttribute("loginInfo").toString() + " 세션 삭제");
		session.removeAttribute("loginInfo");
		return "main";
	}

	// 비밀번호 찾기
	@RequestMapping("/findPwProc.do")
	@ResponseBody
	public String toFindPwProc(String email) {
		System.out.println("사용자 이메일  : " + email);
		JsonObject obj = new JsonObject();
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		
		if(service.findPw(email) == "invalid") {
			obj.addProperty("result", "invalid");
		}else {
			obj.addProperty("result", email);
		}
		return obj.toString();		    
	}

	@RequestMapping("/goMyInfo")  //내 정보 (편집) 가기
	public String goMyInfo(String email, Model model) {
		System.out.println("개인 정보 CON 도착.");
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
		try {			
			MemberDTO dto = service.getMyPageService(mDto.getEmail());
			System.out.println(dto.getEmail());
			System.out.println(dto.getName());
			String poption1 = dto.getPhone().substring(0, 4);
			String poption2 = dto.getPhone().substring(3, 7);
			String poption3 = dto.getPhone().substring(7, 11);
			String boption1 = dto.getBirth().substring(0, 4);
			String boption2 = dto.getBirth().substring(5, 6);
			String boption3 = dto.getBirth().substring(6, 8);
			model.addAttribute("dto", dto);
			model.addAttribute("poption1", poption1);
			model.addAttribute("poption2", poption2);
			model.addAttribute("poption3", poption3);
			model.addAttribute("boption1", boption1);
			model.addAttribute("boption2", boption2);
			model.addAttribute("boption3", boption3);
			return "members/myInformation";
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping("/goMyProfile") //내 프로필(편집) 가기
	public String goMyProfile(String email, Model model) {
		System.out.println("개인 프로필 수정 CON 도착.");
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		try {
			MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
			MemberDTO dto = service.getMyPageService(mDto.getEmail());
			System.out.println(dto.getProfile_img());
			System.out.println(dto.getNickname());
			System.out.println(dto.getProfile_msg());

			model.addAttribute("dto", dto);

			return "members/myProfile";
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping("/withdrawMem") //회원 탈퇴 하기
	public String getout() {
		System.out.println("회원 탈퇴 CON 도착.");
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		try {
			MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
			int result = service.withdrawMemService(mDto.getEmail());
			System.out.println(result);
			if(result> 0) {
				session.invalidate();
				System.out.println("회원탈퇴 성공하셨슴당.");
				return "home";
			}else {
				System.out.println("회원탈퇴 실패하셨슴당.");
				return "error";
			}


		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("입력실패.");
			return "home";
		}


	}

	@RequestMapping("/changeMyInfo") //회원 정보 수정하기
	public String changeInfo(MemberDTO dto) {
		System.out.println("회원 정보 수정 CON 도착.");
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		try {
			int result = 0;
			MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
			if(mDto.getEmail() != null) {
				result = service.changeMyInfoService(mDto.getEmail(), dto);
			}else {
				result = 0;
			}

			if(result > 0) {

				System.out.println("정보변경에 성공하셨슴당.");
				return "home";
			}else {
				System.out.println("정보변경에 실패하셨슴당.");
				return "error";
			}


		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("입력실패.");
			return "redirect:home";
		}
	} 

	@RequestMapping("/changeProfile") //프로필 바꿔버리기
	public String changeMyProfile(MemberDTO dto, MultipartFile profileImg) {
		System.out.println("회원 정보 수정 CON 도착.");
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		String path = session.getServletContext().getRealPath("files");
		MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
		int result = 0;
		try {
			if(profileImg.getOriginalFilename() == "") {
				result = service.changeMyProfileService(mDto.getEmail(), dto,null,path);
			}else {
				result = service.changeMyProfileService(mDto.getEmail(), dto,profileImg,path);
			}


			if(result> 0) {

				System.out.println("정보변경에 성공하셨슴당.");
				return "home";
			}else {
				System.out.println("정보변경에 실패하셨슴당.");
				return "error";
			}


		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("입력실패.");
			return "redirect:home";
		}
	}

	@RequestMapping("/identifyMemPw")
	@ResponseBody
	public String identifyMemPw(String pw) {
		System.out.println("현재 비밀번호 확인 CON 도착"); 
		System.out.println("현재 적은 비번은 "+pw);
		//로그인 세션 테스트 코드 시작
		String email1 = ((MemberDTO)session.getAttribute("loginInfo")).getEmail();
		System.out.println("로그인 세션 값 확인 : " + email1);
		//로그인 세션 테스트 코드 끝
		try {
			MemberDTO mDto = (MemberDTO)session.getAttribute("loginInfo");
			MemberDTO dto = service.identifyMemPwService(mDto.getEmail());
			System.out.println("비번은 "+dto.getPw());
			if(pw.equalsIgnoreCase(dto.getPw())) {
				return "yes";
			}else {
				return "no";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}

	}
}
