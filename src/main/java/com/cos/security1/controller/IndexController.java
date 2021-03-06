package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller //view를 리턴하겠다!!
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCrytPasswordEncoder;
	
	@GetMapping("/test/login")
	@ResponseBody
	public String loginTest(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) { //DI(의존성 주입)
		//@AuthenticationPrincipal 애너테이션을 쓰면 UserDetails 타입을 주입받을 수 있다.
		//그런데 principalDetails을 쓰면 principalDetails은 UserDetails를 상속했기 때문에 principalDetails로 받을 수 있다.
		
		System.out.println("/test/login ====================");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());
		
		
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	
	@GetMapping("/test/oauth/login")
	@ResponseBody
	public String loginOAuthTest(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { //DI(의존성 주입)
		//@AuthenticationPrincipal 애너테이션을 쓰면 UserDetails 타입을 주입받을 수 있다.
		//그런데 principalDetails을 쓰면 principalDetails은 UserDetails를 상속했기 때문에 principalDetails로 받을 수 있다.
		
		System.out.println("/test/oauth/login ====================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : " + oauth2User.getAttributes());
		System.out.println("oauth2User : " + oauth.getAttributes());

		return "OAuth 세션 정보 확인하기";
	}
	
	
	
	//localhost:8080/
	//localhost:8080
	@GetMapping({"","/"})
	public String index() {
		
		//머스테치 기본폴더 src/main/resources/
		//뷰리졸버 설정: templates (prefix), .mustache (suffix)
		//yml 파일에서 생략 가능, 머스테치 의존성을 추가하면 자동 설정 되므로
		return "index";
	}
	
	
	//OAuth 로그인을 해도 PrincipalDetails 받을 수 있고
	//일반 로그인을 해도 PrincipalDetails 를 받을 수 있다
	@ResponseBody
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails:"+principalDetails.getUser());
		return "user";
	}
	
	@ResponseBody
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@ResponseBody
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	//스프링시큐리티가 해당주소를 낚아챔 -> config 파일 만든 후에는 작동 안 함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rewPassword = user.getPassword();
		String encPassword = bCrytPasswordEncoder.encode(rewPassword);
		user.setPassword(encPassword);
		userRepository.save(user); //회원가입 잘됨 비밀번호 : 1234 => 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문이다.
		return "redirect:/loginForm";
	}
	/*
	 * 시큐리티 설정에서 전역적으로 권한 설정을 잡아주고
	 * 메소드 레벨에서 추가하고 싶을 때 이런 애노테이션을 사용하면 된다.
	 * 메소드 레벨에서는 Secured 애너테이션을 주로 사용한다.
	 */
	@Secured("ROLE_ADMIN") //하나만 걸고 싶을 때
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //이 메서드가 실행되기 직전에 권한체크가 실행된다.
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
}
