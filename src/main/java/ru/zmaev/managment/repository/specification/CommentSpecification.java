package ru.zmaev.managment.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.zmaev.managment.model.entity.*;

public class CommentSpecification {

    public static Specification<Comment> hasTaskAndAuthor(Task task, User author) {
        return (root, query, criteriaBuilder) -> {
            Predicate taskPredicate = criteriaBuilder.equal(root.get(Comment_.TASK).get(Task_.ID), task.getId());

            if (author != null) {
                Predicate authorPredicate = criteriaBuilder.equal(root.get(Comment_.AUTHOR).get(User_.ID), author.getId());
                return criteriaBuilder.and(taskPredicate, authorPredicate);
            } else {
                return taskPredicate;
            }
        };
    }
}
