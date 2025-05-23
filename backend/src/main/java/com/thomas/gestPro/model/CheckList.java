package com.thomas.gestPro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="t_check_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckList {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name="card_id",nullable=false)
    @JsonIgnore
    private Card card;

}
