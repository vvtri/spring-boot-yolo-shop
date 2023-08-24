package com.yoloshop.entity;

import javax.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String phone;

    private String address;

    private String name;

    private Boolean isVerified = false;

    @OneToOne
    @JoinColumn(name = "avatar_file_id")
    private  File avatar;
}
