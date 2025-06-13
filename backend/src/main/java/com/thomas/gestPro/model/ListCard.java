package com.thomas.gestPro.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonIgnore
    private Board board;

    @OneToMany(mappedBy = "listCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Card> cardList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListCard listCard = (ListCard) o;
        return id != null && id.equals(listCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
