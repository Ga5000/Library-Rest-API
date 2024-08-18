package com.ga5000.library.services;

import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.dtos.Comment.CreateCommentDTO;
import com.ga5000.library.dtos.Comment.UpdateCommentDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long bookId, Long memberId, CreateCommentDTO createCommentDTO, String currentUsername) throws AccessDeniedException;
    CommentDTO updateComment(Long commentId, UpdateCommentDTO updateCommentDTO, String currentUsername) throws AccessDeniedException;
    List<CommentDTO> getCommentsOfMemberByMemberId(Long memberId);
    List<CommentDTO> getBookCommentsByBookId(Long bookId);
    void deleteComment(Long commentId);

}
