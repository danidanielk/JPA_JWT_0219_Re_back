package com.kim.dani.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String profile;

    @Column(nullable = true)
    private String nickname;

    @OneToMany(mappedBy = "users" , cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Follower> follower = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Following> following = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LikeYou> likeyou = new ArrayList<>();


}
