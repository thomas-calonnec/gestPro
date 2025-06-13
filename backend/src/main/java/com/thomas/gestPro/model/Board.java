package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_board")
@Getter
@Setter
@Builder
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
    @Builder.Default
    private List<ListCard> listCards = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tj_user_boards",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private List<User> members = new ArrayList<>();

    @ManyToMany(mappedBy = "boards", cascade = CascadeType.MERGE)
    @Builder.Default
    private List<Workspace> workspaces = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id != null && id.equals(board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setMembers(List<User> newMembers) {
        // Supprime les anciennes relations côté User
        if (this.members != null) {
            this.members.forEach(user -> user.getBoards().remove(this));
        }

        // Met à jour la liste des membres
        this.members = newMembers;

        // Ajoute cette board aux users
        if (newMembers != null) {
            newMembers.forEach(user -> {
                if (!user.getBoards().contains(this)) {
                    user.getBoards().add(this);
                }
            });
        }
    }
}
