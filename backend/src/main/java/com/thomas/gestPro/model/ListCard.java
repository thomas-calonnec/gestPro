package com.thomas.gestPro.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_list_card")
public class ListCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int orderIndex;
    private Boolean isArchived;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIdentityReference
    private Board board;

    @OneToMany(mappedBy = "listCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Card> cardList = new ArrayList<>();
}
