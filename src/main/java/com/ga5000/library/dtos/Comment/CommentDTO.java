package com.ga5000.library.dtos.Comment;

import java.time.LocalDateTime;

public record CommentDTO(String member, LocalDateTime createdAt, String content) {
}
