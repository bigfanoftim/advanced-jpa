package com.bigfanoftim.advancedjpa.user.domain;

import com.bigfanoftim.advancedjpa.team.domain.Team;
import com.bigfanoftim.advancedjpa.team.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired EntityManager em;

    @Autowired UserRepository userRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void simpleTest() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        userRepository.save(userA);

        User user = userRepository.findById(userA.getId()).get();

        assertThat(user.getId()).isEqualTo(userA.getId());
        assertThat(user.getAge()).isEqualTo(userA.getAge());
        assertThat(user).isEqualTo(userA);
    }

    @Test
    public void findByName_namedQuery() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        userRepository.save(userA);

        List<User> allUsers = userRepository.findByName("userA");

        assertThat(allUsers.get(0).getId()).isEqualTo(userA.getId());
        assertThat(allUsers.get(0).getName()).isEqualTo(userA.getName());
        assertThat(allUsers.get(0).getAge()).isEqualTo(userA.getAge());
        assertThat(allUsers.get(0)).isEqualTo(userA);
    }

    @Test
    public void findUser_query() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        userRepository.save(userA);

        List<User> allUsers = userRepository.findUser("userA", 10);

        assertThat(allUsers.get(0).getId()).isEqualTo(userA.getId());
        assertThat(allUsers.get(0).getName()).isEqualTo(userA.getName());
        assertThat(allUsers.get(0).getAge()).isEqualTo(userA.getAge());
        assertThat(allUsers.get(0)).isEqualTo(userA);
    }

    @Test
    public void findByNames() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        User userB = User.builder()
                .name("userB")
                .age(20)
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        List<User> users = userRepository.findByNames(Arrays.asList("userA", "userB"));

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void findByAgeGreaterThan() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        User userB = User.builder()
                .name("userB")
                .age(20)
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        List<User> allUsers = userRepository.findByAgeGreaterThan(10);

        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.get(0).getId()).isEqualTo(userB.getId());
        assertThat(allUsers.get(0).getName()).isEqualTo(userB.getName());
        assertThat(allUsers.get(0).getAge()).isEqualTo(userB.getAge());
    }

    @Test
    public void findByAgeGreaterThanEqual() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        User userB = User.builder()
                .name("userB")
                .age(20)
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        List<User> allUsers = userRepository.findByAgeGreaterThanEqual(10);

        assertThat(allUsers.size()).isEqualTo(2);
    }

    @Test
    public void findMemberByName() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        User userB = User.builder()
                .name("userB")
                .age(20)
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        User userA1 = userRepository.findMemberByName("userA");

        assertThat(userA1.getId()).isEqualTo(userA.getId());
        assertThat(userA1.getName()).isEqualTo(userA.getName());
        assertThat(userA1.getAge()).isEqualTo(userA.getAge());
        assertThat(userA1).isEqualTo(userA);
    }

    /**
     * 순수 JPA에선 NoResultException 처리
     * 하지만 Spring Data JPA는 null로 반환
     * 따라서 없을 수도 있음을 의미하는 Optional로 반환하는 것이 좋을 수 있음
     *
     * 만약 객체 하나만 반환해야 하는데 2개 이상이 반환되면 repository에 맞는 예외 발생 (JPA, Mongo..)
     * Spring은 해당 예외를 Spring에 맞는 예외로 변경한다. (IncorrectResultSizeDataAccessException)
     * 따라서 어떤 Data Repository를 사용하더라도 클라이언트는 같은 예외로 처리할 수 있다.
     */
    @Test
    public void findMemberByName_결과_없으면_null() throws Exception {
        User userA = User.builder()
                .name("userA")
                .age(10)
                .build();
        User userB = User.builder()
                .name("userB")
                .age(20)
                .build();
        userRepository.save(userA);
        userRepository.save(userB);

        User userA1 = userRepository.findMemberByName("unknown");
        assertThat(userA1).isNull();
    }

    @Test
    public void page_findByAge() throws Exception {
        //given
        userRepository.save(new User("A1", 20));
        userRepository.save(new User("A2", 20));
        userRepository.save(new User("A3", 20));
        userRepository.save(new User("A4", 20));
        userRepository.save(new User("A5", 20));
        userRepository.save(new User("A6", 20));
        userRepository.save(new User("A7", 20));
        userRepository.save(new User("A8", 20));
        userRepository.save(new User("A9", 20));

        int age = 20;
        int size = 4;
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "name"));

        long count = userRepository.count();
        long totalPages = count / size + ((count % size > 0 ? 1 : 0));

        //when
        Page<User> page = userRepository.findByAge(age, pageRequest);
        List<User> content = page.getContent();

        //then
        assertThat(content.size()).isEqualTo(4);
        assertThat(page.getTotalElements()).isEqualTo(count);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(totalPages);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void page_findSliceByAge() throws Exception {
        //given
        userRepository.save(new User("A1", 20));
        userRepository.save(new User("A2", 20));
        userRepository.save(new User("A3", 20));
        userRepository.save(new User("A4", 20));
        userRepository.save(new User("A5", 20));
        userRepository.save(new User("A6", 20));
        userRepository.save(new User("A7", 20));
        userRepository.save(new User("A8", 20));
        userRepository.save(new User("A9", 20));

        int age = 20;
        int size = 4;
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Slice<User> page = userRepository.findByAge(age, pageRequest);
        List<User> content = page.getContent();

        //then
        assertThat(content.size()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void findListByAge() throws Exception {
        //given
        userRepository.save(new User("A1", 10));
        userRepository.save(new User("A2", 15));
        userRepository.save(new User("A3", 20));
        userRepository.save(new User("A4", 26));
        userRepository.save(new User("A5", 39));

        //when
        int resultCount = userRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);

        User user = userRepository.findByName("A3").get(0);
        assertThat(user.getAge()).isEqualTo(21);
    }

    @Test
    public void findUserWithTeamByName() throws Exception {
        //given
        Team team = new Team("team");
        teamRepository.save(team);

        userRepository.save(new User("A1", 10 ,team));
        userRepository.save(new User("A2", 15, team));

        em.flush();
        em.clear();

        //when
        List<User> users = userRepository.findUserWithTeamByName("A1");

        for (User user : users) {
            System.out.println("user.getName = " + user.getName());
            System.out.println("user.teamClass = " + user.getTeam().getClass());
        }

        //then
    }

    @Test
    public void findUserByJoin() throws Exception {
        //given
        Team team = new Team("team");
        teamRepository.save(team);

        userRepository.save(new User("A1", 10 ,team));
        userRepository.save(new User("A2", 15, team));

        em.flush();
        em.clear();

        //when
        List<User> users = userRepository.findUserByJoin();

        for (User user : users) {
            System.out.println("user.getTeam().getName() = " + user.getTeam().getName());
        }

        //then
    }
}