package com.ga5000.library.dtos.Book;

import com.ga5000.library.dtos.Comment.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;

public record BookWithCommentsDTO(Long id, String title, String isbn, String author, List<String> genres,
                                  int availableCopies, int totalCopies,
                                  LocalDateTime publishedDate, List<CommentDTO> comments){
}
