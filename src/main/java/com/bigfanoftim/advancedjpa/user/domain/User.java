package com.bigfanoftim.advancedjpa.user.domain;

import javax.persistence.*;

import com.bigfanoftim.advancedjpa.common.base.BaseEntity;
import com.bigfanoftim.advancedjpa.common.base.BaseTimeEntity;
import com.bigfanoftim.advancedjpa.team.domain.Team;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString(of = {"id", "name"})
@Entity
@Table(name = "users")
@NamedQuery(
        name="User.findByName",
        query="select u from User u where u.name = :name"
)
@NamedEntityGraph(name = "User.all", attributeNodes = @NamedAttributeNode("team"))
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public User(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
    }

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
