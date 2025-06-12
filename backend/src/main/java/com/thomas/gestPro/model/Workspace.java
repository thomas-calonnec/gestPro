package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_workspace")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Date updateAt;
    private Boolean isFavorite;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tj_workspace_board",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    @Builder.Default
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tj_user_workspace",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private List<User> users = new ArrayList<>();

  
}

