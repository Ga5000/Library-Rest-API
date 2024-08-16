package com.ga5000.library.repositories;

import com.ga5000.library.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByMemberId(Long memberId);
    List<Comment> findByBookId(Long bookId);
}
