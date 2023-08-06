package com.bigfanoftim.advancedjpa.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

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
}