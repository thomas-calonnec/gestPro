package com.thomas.gestPro.model;

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
@Table(name="t_workspace")
public class Workspace {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Date updateAt;

    private Boolean isFavorite;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JoinTable(name="tj_workspace_board",
            joinColumns = @JoinColumn(name="workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tj_user_workspace",
            joinColumns = @JoinColumn(name="workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<User> users = new ArrayList<>();
}
