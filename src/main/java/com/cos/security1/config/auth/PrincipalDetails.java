package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킵니다.
//로그인을 진행이 완료가 되면 시큐리티가 가지고 있는 시큐리티의 session을 만들어줍니다.
//세션 공간은 똑같은데 시큐리티가 자신만의 세션 공간을 가진다. 즉 키 값으로 구분한다는 건데
//Security ContextHolder가 키 값에다가 세션 정보를 저장시킵니다.
//세션에 들어갈 수 있는 정보는 오브젝트 타입 Authentication 객체여야 한다.
//Authentication 안에 User 정보가 있어야 된다.
//User오브젝트타입은 UserDetatils 타입 객체여야 한다.

//Security Session에 저장될 수 있는 객체가 Authentication 으로 정해져 있다. 
//이 객체 안에 User 정보를 저장할 때 UserDetails 타입이어야 한다.

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{
	
	private User user; //콤포지션
	private Map<String, Object> attributes;
	
	//일반 로그인용 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth 로그인 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	

	//해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return user.getRole();
			}
			
		});
		
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호 너무 오래사용한 아니니?
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	//계정이 활성화 되었니
	@Override
	public boolean isEnabled() {
		
		//예를 들어서 우리 사이트에서 1년 동안 회원이 로그인을 안 하면 휴면 계정으로 하기로 했다면
		//User Entity안에 Timestap loginDate; 필드를 넣어준다.
		//그리고 유저의 getLoginDate();를 가져와서 
		//현재시간 - 로긴시간 => 1년을 초과하면 return false;
		
		return true;
	}


	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}


	@Override
	public String getName() {
		return null;
	}

}
