package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller //view를 리턴하겠다!!
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCrytPasswordEncoder;
	
	
	
	//localhost:8080/
	//localhost:8080
	@GetMapping({"","/"})
	public String index() {
		
		//머스테치 기본폴더 src/main/resources/
		//뷰리졸버 설정: templates (prefix), .mustache (suffix)
		//yml 파일에서 생략 가능, 머스테치 의존성을 추가하면 자동 설정 되므로
		return "index";
	}
	
	@ResponseBody
	@GetMapping("/user")
	public String user() {
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
	
}
