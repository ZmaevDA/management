package ru.zmaev.managment.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "_user")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "keycloak_id")
    private UUID keycloakId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "author")
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks;
}
