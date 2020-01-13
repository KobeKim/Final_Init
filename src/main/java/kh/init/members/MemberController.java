package kh.init.members;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
			session.setAttribute("loginInfo", email); // 세션 로그인정보 담기
			return "main";
		}else {
			return "error";
		}
	}
	
	// 비밀번호 찾기 페이지 로드
	@RequestMapping("/findPw.do")
	public String toFindPw() {
		return "members/findPw";
	}
	
	// 비밀번호 찾기
	@RequestMapping("/findPwProc.do")
	public String toFindPwProc(String email) {
		System.out.println("사용자 이메일  : " + email);
		if(service.findPw(email) == "invalid") {
			return "error";
		}else {
			return "main";
		}
//		    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
//		     protected PasswordAuthentication getPasswordAuthentication() {
//		      return new PasswordAuthentication(user, password);
//		     }
//		    });
//
//		    // Compose the message
//		    try {
//		     MimeMessage message = new MimeMessage(session);
//		     message.setFrom(new InternetAddress(user));
//		     message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//		     // Subject
//		     message.setSubject("[Subject] Java Mail Test");
//		     
//		     // Text
//		     message.setText("Simple mail test..");
//
//		     // send the message
//		     Transport.send(message);
//		     System.out.println("message sent successfully...");
//
//		    } catch (MessagingException e) {
//		     e.printStackTrace();
//		    }
	}
	
	@RequestMapping("/goMyInfo")
	public String goMyInfo(String email, Model model) {
		System.out.println("개인 정보 CON 도착.");
		try {
		MemberDTO dto = service.getMyPageService("kks@naver.com");
		System.out.println(dto.getEmail());
		System.out.println(dto.getName());
		String poption1 = dto.getPhone().substring(0, 2);
		String poption2 = dto.getPhone().substring(3, 7);
		String poption3 = dto.getPhone().substring(7, 11);
		String boption1 = dto.getBirth().substring(0, 2);
		String boption2 = dto.getBirth().substring(3, 4);
		String boption3 = dto.getBirth().substring(5, 6);
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
     
	@RequestMapping("/goMyProfile")
	public String goMyProfile(String email, Model model) {
		System.out.println("개인 프로필 수정 CON 도착.");
		try {
		MemberDTO dto = service.getMyPageService("kks@naver.com");
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
	
	@RequestMapping(value = "/getMyPage", produces ="text/html; charset=utf-8")
	public String getMyPage() {
		System.out.println("마이페이지 CON 도착.");
		ModelAndView mav = new ModelAndView();
		try {
			MemberDTO dto = service.getMyPageService((String)session.getAttribute("loginInfo"));

			mav.addObject("dto",dto);

			return "good";
		}catch(Exception e) {
			e.printStackTrace();
			return "job";
		}
	}
	@RequestMapping("/withdrawMem")
	public String getout() {
		System.out.println("회원 탈퇴 CON 도착.");
		try {
			int result = service.withdrawMemService("kks@naver.com");
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

	@RequestMapping("/changeMyInfo")
	public String changeInfo(MemberDTO dto) {
		System.out.println("회원 정보 수정 CON 도착.");
		try {
			
			
			int result = service.changeMyInfoService("kks@naver.com", dto);
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
	
	@RequestMapping("/changeProfile")
	public String changeMyProfile(MemberDTO dto, MultipartFile profileImg) {
		System.out.println("회원 정보 수정 CON 도착.");
		String path = session.getServletContext().getRealPath("files");
		int result = 0;
		try {
			if(profileImg.getOriginalFilename() == "") {
				result = service.changeMyProfileService("kks@naver.com", dto,null,path);
			}else {
				result = service.changeMyProfileService("kks@naver.com", dto,profileImg,path);
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
}
