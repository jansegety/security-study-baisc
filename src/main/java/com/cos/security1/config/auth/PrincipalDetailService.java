package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;


// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	//여기서 중요한 점은 loginForm 페이지에서
	//사용자 계정 form 파라미터 이름을 username이라고 하지 않고 id or account or emial or username2 이렇게 하면
	//이 파라미터로 받아지지 않는다.
	
	//시큐리티 session(내부 Authentication(내부 UserDetatils))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}

	
	
}
