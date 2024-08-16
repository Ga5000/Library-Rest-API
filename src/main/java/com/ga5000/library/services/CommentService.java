package com.ga5000.library.services;

import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.dtos.Comment.CreateCommentDTO;
import com.ga5000.library.dtos.Comment.UpdateCommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long bookId, Long memberId, CreateCommentDTO createCommentDTO);
    CommentDTO updateComment(Long commentId, UpdateCommentDTO updateCommentDTO);
    List<CommentDTO> getCommentsOfMemberByMemberId(Long memberId);
    List<CommentDTO> getBookCommentsByBookId(Long bookId);
    void deleteComment(Long commentId);

}
