package com.thomas.gestPro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    private Long ownerId;

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<ListCard> listCards = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.MERGE)
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id",
            scope = Board.class
    )
    private List<User> members = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.MERGE)
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id",
            scope = Board.class
    )
    private List<Workspace> workspaces = new ArrayList<>();
}
