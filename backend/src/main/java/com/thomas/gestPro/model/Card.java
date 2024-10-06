package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String cardName;
    private String cardDescription;
    private LocalDate cardDeadline;

    @ManyToOne
    @JoinColumn(name = "list_card_id", nullable = false)
    private ListCard listCard;

    @ManyToMany
    @JoinTable(name="tj_label_card",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private Set<Label> labels = new HashSet<>();

    @ManyToMany(mappedBy = "cards")
    private Set<Users> users = new HashSet<>();
}
