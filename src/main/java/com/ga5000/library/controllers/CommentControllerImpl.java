package com.ga5000.library.controllers;

import com.ga5000.library.services.CommentServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentControllerImpl implements CommentController{
    private final CommentServiceImpl commentService;

    public CommentControllerImpl(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }
}
