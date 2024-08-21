package com.ga5000.library.dtos.Book;

import com.ga5000.library.dtos.Comment.CommentDTO;

import java.util.Date;
import java.util.List;

public record BookWithCommentsDTO(Long id, String title, String isbn, String author, List<String> genres,
                                  int availableCopies, int totalCopies,
                                  Date publishedDate, List<CommentDTO> comments){
}
