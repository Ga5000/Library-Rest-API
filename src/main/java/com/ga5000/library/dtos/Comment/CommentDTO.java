package com.ga5000.library.dtos.Comment;

import java.util.Date;

public record CommentDTO(String member, Date createdAt, String content) {
}
