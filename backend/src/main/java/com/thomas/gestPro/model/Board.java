package com.thomas.gestPro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String description;
    private Date lastUpdated;
    private Integer cardCount;
    private Integer ownerId;

    @OneToMany(mappedBy = "board",cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ListCard> listCards = new ArrayList<>();

    @ManyToMany(mappedBy = "boards",cascade = CascadeType.PERSIST)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<User> members = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.PERSIST)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Workspace> workspaces = new ArrayList<>();


}
