package com.be_nlu_echo.repository;

import com.be_nlu_echo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countDistinctEchoIdByUserId(Long userId);

}
