package com.bigfanoftim.advancedjpa.user.domain;

import com.bigfanoftim.advancedjpa.team.domain.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.bigfanoftim.advancedjpa.team.domain.QTeam.*;
import static com.bigfanoftim.advancedjpa.user.domain.QUser.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class UserQueryDslTest {

    @PersistenceUnit
    EntityManagerFactory emf;

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory query;

    @BeforeEach
    public void setUp() {
        query = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        User userA = new User("A", 20, teamA);
        User userB = new User("B", 22, teamA);
        User userC = new User("C", 23, teamA);
        User userD = new User("D", 28, teamB);
        User userE = new User("E", 30, teamB);
        User userF = new User("F", 40, teamB);
        em.persist(userA);
        em.persist(userB);
        em.persist(userC);
        em.persist(userD);
        em.persist(userE);
        em.persist(userF);

        em.clear();
    }

    @Test
    public void querydsl() throws Exception {
        User findUser = query
                .select(user)
                .from(user)
                .where(user.name.eq("A"))
                .fetchOne();

        assertThat(findUser.getName()).isEqualTo("A");
        assertThat(findUser.getAge()).isEqualTo(20);
        assertThat(findUser.getTeam().getName()).isEqualTo("teamA");
    }

    @Test
    public void search() throws Exception {
        User findUser = query
                .select(user)
                .from(user)
                .where(user.name.eq("A")
                        .and(user.age.eq(15)))
                .fetchOne();

        assertThat(findUser).isNull();
    }

    @Test
    public void searchAndParam() throws Exception {
        User findUser = query
                .select(user)
                .from(user)
                .where(
                        user.name.eq("A"),
                        user.age.between(10, 30) // 이렇게 작성하면 null 값인 parameter를 무시하기 때문에 동적 쿼리 활용 시 굉장히 유용하다.
                )
                .fetchOne();

        assertThat(findUser.getName()).isEqualTo("A");
        assertThat(findUser.getAge()).isEqualTo(20);
    }

    @Test
    public void sort() throws Exception {
        em.persist(new User(null, 100));
        em.persist(new User("userA", 100));
        em.persist(new User("userB", 100));

        List<User> result = query
                .select(user)
                .from(user)
                .orderBy(user.age.desc(), user.name.asc().nullsLast())
                .fetch();

        assertThat(result.get(0).getName()).isEqualTo("userA");
        assertThat(result.get(1).getName()).isEqualTo("userB");
        assertThat(result.get(2).getName()).isNull();
    }

    @Test
    public void paging1() throws Exception {
        List<User> result = query
                .select(user)
                .from(user)
                .orderBy(user.age.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    /**
     * where 조건 붙기 시작하면 count query는 따로 조회하는 것이 성능상 이점이 있을 수 있다.
     */
    @Test
    public void paging2() throws Exception {
        QueryResults<User> queryResults = query
                .select(user)
                .from(user)
                .orderBy(user.age.desc())
                .offset(1)
                .limit(5)
                .fetchResults();

        long total = queryResults.getTotal();
        long limit = queryResults.getLimit();
        long offset = queryResults.getOffset();
        List<User> results = queryResults.getResults();

        assertThat(total).isEqualTo(6);
        assertThat(limit).isEqualTo(5);
        assertThat(offset).isEqualTo(1);
        assertThat(results.size()).isEqualTo(5);
    }

    @Test
    public void aggregation() throws Exception {
        Tuple tuple = query
                .select(
                        user.count(),
                        user.age.sum(),
                        user.age.max(),
                        user.age.min(),
                        user.age.avg()
                )
                .from(user)
                .fetchOne();

        assertThat(tuple.get(user.count())).isEqualTo(6);
        assertThat(tuple.get(user.age.sum())).isEqualTo(163);
        assertThat(tuple.get(user.age.max())).isEqualTo(40);
        assertThat(tuple.get(user.age.min())).isEqualTo(20);
        assertThat(tuple.get(user.age.avg())).isGreaterThan(27).isLessThan(28);
    }

    @Test
    public void groupBy() throws Exception {
        List<Tuple> result = query
                .select(team.name, user.age.sum())
                .from(user)
                .join(user.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamATuple = result.get(0);
        Tuple teamBTuple = result.get(1);

        assertThat(teamATuple.get(team.name)).isEqualTo("teamA");
        assertThat(teamATuple.get(user.age.sum())).isEqualTo(65);

        assertThat(teamBTuple.get(team.name)).isEqualTo("teamB");
        assertThat(teamBTuple.get(user.age.sum())).isEqualTo(98);
    }

    /**
     * teamA에 속한 모든 회원
     */
    @Test
    public void join() throws Exception {
        List<User> result = query
                .select(user)
                .from(user)
                .join(user.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("name")
                .containsExactly("A", "B", "C");

        /**
         * fetch join이 아니기 때문에 team에 대한 정보는 lazy loading
         * select에 user만 있음을 인지해야 한다.
         */
        for (User user : result) {
            System.out.println("user.getTeam().getName() = " + user.getTeam().getName());
        }
    }

    @Test
    public void thetaJoin() throws Exception {
        em.persist(new User("teamA", 100));
        em.persist(new User("teamB", 100));
        em.persist(new User("teamC", 100));

        List<User> result = query
                .select(user)
                .from(user, team)
                .where(user.name.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("name")
                .containsExactly("teamA", "teamB");
    }

    @Test
    public void joinFiltering() throws Exception {
        em.persist(new User("TEST1", 100));
        em.persist(new User("TEST2", 100));

        List<Tuple> fetch = query
                .select(user, team)
                .from(user)
                .leftJoin(user.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 관계(foreign key 참조)가 아닌 다른 것으로 join 하는 경우
     */
    @Test
    public void joinNoRelation() throws Exception {
        em.persist(new User("teamA", 100));
        em.persist(new User("teamB", 100));
        em.persist(new User("teamC", 100));

        List<Tuple> result = query
                .select(user, team)
                .from(user)
                .leftJoin(team).on(user.name.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void withoutFetchJoin() throws Exception {
        User findUser = query
                .selectFrom(user)
                .join(user.team, team)
                .where(user.name.eq("A"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findUser.getTeam());

        assertThat(loaded).as("fetch join 미적용").isFalse();
    }

    @Test
    public void withFetchJoin() throws Exception {
        User findUser = query
                .selectFrom(user)
                .join(user.team, team).fetchJoin()
                .where(user.name.eq("A"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findUser.getTeam());

        assertThat(loaded).as("fetch join 적용").isTrue();
    }
}