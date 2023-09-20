package com.LTUC.codefellowship.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ApplicationUser userId;

    private String body;

    private LocalDate createdAt;

    public Post(){}

    public Post(ApplicationUser userId, String body, LocalDate createdAt) {
        this.userId = userId;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public ApplicationUser getUserId() {
        return userId;
    }

//    public void setUserId(ApplicationUser userId) {
//        this.userId = userId;
//    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
