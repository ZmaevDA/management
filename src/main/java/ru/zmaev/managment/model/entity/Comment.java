package ru.zmaev.managment.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private String createdAt;
}
