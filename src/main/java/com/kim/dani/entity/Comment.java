package com.kim.dani.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "users_id")
    Users users;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "board_id")
    Board board;
}



