package ru.zmaev.managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.zmaev.managment.model.entity.Task;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
}
