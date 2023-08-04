package com.bigfanoftim.advancedjpa.order.domain;

import com.bigfanoftim.advancedjpa.course.domain.Course;
import com.bigfanoftim.advancedjpa.course.domain.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void course_item_조회() throws Exception {
        //given
        Course course = Course.courseBuilder()
                .name("Spring Boot 강의")
                .price(30000)
                .description("Spring Boot 강의에 오신 것을 환영합니다.")
                .thumbnail("https://www.bigfanoftim.com/course/spring-boot")
                .build();
        courseRepository.save(course);

        OrderItem orderItem = OrderItem.builder()
                .item(course)
                .build();
        orderItemRepository.save(orderItem);

        //when
        List<OrderItem> allOrderItems = orderItemRepository.findAll();
        Course findCourse = (Course) allOrderItems.get(0).getItem();

        //then
        assertThat(findCourse.getDescription()).isEqualTo("Spring Boot 강의에 오신 것을 환영합니다.");
        assertThat(findCourse.getThumbnail()).isEqualTo("https://www.bigfanoftim.com/course/spring-boot");
    }
}