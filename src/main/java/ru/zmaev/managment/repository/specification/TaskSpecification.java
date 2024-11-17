package ru.zmaev.managment.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.zmaev.managment.model.dto.response.TaskFilterRequest;
import ru.zmaev.managment.model.entity.Task;

import jakarta.persistence.criteria.*;
import ru.zmaev.managment.model.entity.Task_;
import ru.zmaev.managment.model.entity.User_;

import java.util.UUID;

public class TaskSpecification {

    public static Specification<Task> filterTasks(TaskFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (filterRequest == null) {
                return predicate;
            }
            if (filterRequest.getId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Task_.ID),
                        UUID.fromString(filterRequest.getId())));
            }
            if (filterRequest.getTitle() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get(Task_.title)),
                        "%" + filterRequest.getTitle().toLowerCase() + "%"));
            }
            if (filterRequest.getDescription() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get(Task_.description)),
                        "%" + filterRequest.getDescription().toLowerCase() + "%"));
            }
            if (filterRequest.getStatus() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Task_.status),
                        filterRequest.getStatus()));
            }
            if (filterRequest.getPriority() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Task_.priority),
                        filterRequest.getPriority()));
            }
            if (filterRequest.getAuthorId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Task_.author).get(User_.ID),
                        filterRequest.getAuthorId()));
            }
            if (filterRequest.getAssigneeId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(Task_.assignee).get(User_.ID),
                        filterRequest.getAssigneeId()));
            }

            return predicate;
        };
    }
}
