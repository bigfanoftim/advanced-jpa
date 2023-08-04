package com.bigfanoftim.advancedjpa.course.domain;

import com.bigfanoftim.advancedjpa.item.domain.Item;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("COURSE")
@Table(name = "courses")
public class Course extends Item {

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnail;

    @Builder(builderMethodName = "courseBuilder")
    public Course(String name, int price, String description, String thumbnail) {
        super(name, price);
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
