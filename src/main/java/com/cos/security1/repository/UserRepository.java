package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;


//CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC되요. 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{

	//findBy규칙 그리고 Username은 문법
	//select * from user where username = 1? 이게 호출된다.
	//1?에는 파라미터에 username이 넘어간다.
	public User findByUsername(String username); //Jpa Query methods 를 공부해보면 알 수 있다.

	
	
}
