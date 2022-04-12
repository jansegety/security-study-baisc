package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 애를 활성화 하면 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	

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
			.defaultSuccessUrl("/");
			
	}

	
	
}
