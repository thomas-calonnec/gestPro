package com.thomas.gestPro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String userPassword;
    private String userEmail;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Workspace> workspaces = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    @JoinTable(name="tj_user_card",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private Set<Card> cards = new HashSet<>();
}
