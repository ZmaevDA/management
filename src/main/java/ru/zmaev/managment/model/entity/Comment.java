package ru.zmaev.managment.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_seq")
    @SequenceGenerator(name = "comment_id_seq", sequenceName = "comment_id_seq")
    private String id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    private User author;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private String createdAt;
}
