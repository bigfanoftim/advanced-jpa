package com.bigfanoftim.advancedjpa.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 생략해도 가장 먼저 JpaRepository의 Domain class를 기준으로 NamedQuery 이름 매칭되는걸로 사용
     * 만약 NamedQuery가 없으면 메소드 이름으로 쿼리 생성 전력 사용
     */
    @Query(name = "User.findByName")
    List<User> findByName(@Param("name") String name);

    /**
     * !!위의 NamedQuery와 마찬가지로 애플리케이션 실행 시점에 문법 오류를 발견할 수 있다.!!
     */
    @Query("select u from User u where u.name = :name and u.age = :age")
    List<User> findUser(@Param("name") String name, @Param("age") int age);

    List<User> findByAgeGreaterThan(int age);

    List<User> findByAgeGreaterThanEqual(int age);
}
