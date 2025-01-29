package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private Integer hours;
    private Integer minutes;
    private Boolean isCompleted;
    private Boolean isDateActivated;

    @ManyToOne
    @JoinColumn(name = "list_card_id", nullable = false)
    private ListCard listCard;

    @ManyToMany
    @JoinTable(name="tj_label_card",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels = new ArrayList<>();

    @OneToMany(mappedBy = "card",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<CheckList> checkList = new ArrayList<>();

    @ManyToMany(mappedBy = "cards")
    private List<User> users = new ArrayList<>();
}
