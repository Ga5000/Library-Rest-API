package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.dtos.Comment.CreateCommentDTO;
import com.ga5000.library.dtos.Comment.UpdateCommentDTO;
import com.ga5000.library.services.CommentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentControllerImpl implements CommentController{
    private final CommentServiceImpl commentService;

    public CommentControllerImpl(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{bookId}/{memberId}")
    @Override
    public ResponseEntity<CommentDTO> createComment(@PathVariable("bookId") Long bookId,
                                                    @PathVariable("memberId") Long memberId,
                                                    @RequestBody CreateCommentDTO createCommentDTO,
                                                    Authentication authentication) {
        String currentUsername = authentication.getName();
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(commentService.createComment(bookId,memberId,createCommentDTO, currentUsername));
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{commentId}")
    @Override
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("commentId") Long commentId,
                                                    @RequestBody UpdateCommentDTO updateCommentDTO,
                                                    Authentication authentication) {
        String currentUsername = authentication.getName();
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(commentService.updateComment(commentId, updateCommentDTO,currentUsername));
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @DeleteMapping("{commentId}")
    @Override
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
