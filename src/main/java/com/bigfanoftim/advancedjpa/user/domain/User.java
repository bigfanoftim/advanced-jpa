package com.bigfanoftim.advancedjpa.user.domain;

import javax.persistence.*;

import com.bigfanoftim.advancedjpa.team.domain.Team;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
@Entity
@Table(name = "users")
@NamedQuery(
        name="User.findByName",
        query="select u from User u where u.name = :name"
)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getUsers().add(this);
    }
}
