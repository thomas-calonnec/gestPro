package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private String description;
    private Date lastUpdated;
    private Integer cardCount;
    private String status;
    private Long ownerId;

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    private List<ListCard> listCards = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.MERGE)

    private List<User> members = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.MERGE)

    private List<Workspace> workspaces = new ArrayList<>();
}
