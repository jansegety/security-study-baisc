package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity // 애를 활성화 하면 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * OAuth 과정
	 * 1코드받기(인증), 2엑세스토큰(권한), 3사용자 프로필 정보를 가져와서, 4 그 정보를 토대로 회원가입을 자동으로 진행
	 * 4-2 (이메일, 전화 번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip 등급, 일반 등급)
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); 
		
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //인증만 되면 들아갈 수 있는 주소!!
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and() 
			.formLogin() //권한이 없는 페이지에 대해서 로그인 페이지로 리다이랙트 되도록 지정
			.loginPage("/loginForm")
			//.usernameParameter("usename2") 만약 UserDetailsService애서 username2로 파라미터로 받고 싶다면 여기서 이렇게 설정해주면 된다.
			.loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다. 즉 컨트롤러에 /login을 안 만들어도 된다.
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService); //구글 로그인이 완료된 뒤의 후처리 필요  Tip 구글은 코드X, (약세스토큰+사용자프로필 정보)를 한번에 받아줌
		
			
	}

	
	
}
