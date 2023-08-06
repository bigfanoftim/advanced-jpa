package com.bigfanoftim.advancedjpa.team.domain;

import com.bigfanoftim.advancedjpa.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private List<User> users = new ArrayList<>();

    @Builder
    public Team(String name) {
        this.name = name;
    }
}
