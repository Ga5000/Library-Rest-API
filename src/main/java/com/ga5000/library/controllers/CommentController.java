package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.dtos.Comment.CreateCommentDTO;
import com.ga5000.library.dtos.Comment.UpdateCommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentController {
    ResponseEntity<CommentDTO> createComment(Long bookId, Long memberId, CreateCommentDTO createCommentDTO, Authentication authentication);
    ResponseEntity<CommentDTO> updateComment(Long commentId, UpdateCommentDTO updateCommentDTO, Authentication authentication);

    ResponseEntity<Void> deleteComment(Long commentId);

}
