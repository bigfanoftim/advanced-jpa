package com.bigfanoftim.advancedjpa.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    /**
     * List 대신 상위 개념인 Collection을 사용하면 더 포괄적으로 사용할 수 있음
     */
    @Query("select u from User u where u.name in :names")
    List<User> findByNames(@Param("names") List<String> names);

    List<User> findByAgeGreaterThan(int age);

    List<User> findByAgeGreaterThanEqual(int age);

    /**
     * return type을 상황에 맞게 설정할 수 있다.
     * name이 unique하면 다음과 같이 단건 조회로 설정할 수 있음.
     * find 뒤에는 아무거나 작성해도 됨
     *
     * TEST code 참고 : findMemberByName_결과_없으면_null
     */
    User findMemberByName(String name);

    /**
     * return type을 상황에 맞게 설정할 수 있다.
     * Optional도 가능
     * find 뒤에는 아무거나 작성해도 됨
     */
    Optional<User> findOptionalUserByName(Long aLong);

    Page<User> findByAge(int age, Pageable pageable);

    /**
     * 성능 최적화를 위해 countQuery를 분리할 수도 있어야 한다.
     *
     * 이런 상황에서는 left join이 실제 user 총 count를 변화시키지 않기 때문에 count query에서 굳이 join이 추가될 필요가 없다.
     */
    @Query(value = "select u from User u left join u.team t",
            countQuery = "select count(u) from User u")
    Page<User> findOptimizationPageByAge(int age, Pageable pageable);

    /**
     * count query는 호출하지 않고 hasNext 정도만 판단한다. 주어진 limit + 1로 조회
     */
    Slice<User> findSliceByAge(int age, Pageable pageable);

    /**
     * 엔티티로 반환할 수도 있다.
     */
    List<User> findListByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.age = u.age + 1 where u.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * EntityGraph를 Named로 활용할 수 있다.
     */
//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("User.all")
    List<User> findUserWithTeamByName(@Param("name") String name);

    @Query("select u from User u left join u.team t")
    List<User> findUserByJoin();
}
