package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String password;
    private String email;
    private String pictureUrl;

    public User(String email) {
        this.email = email;
    }

    @ManyToMany(mappedBy = "users")
    @Builder.Default
    private List<Workspace> workspaces = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "tj_user_card",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    @Builder.Default
    private List<Card> cards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinTable(name = "tj_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

}
