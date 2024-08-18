package com.ga5000.library.dtos.Book;

import java.time.LocalDateTime;
import java.util.List;

public record CreateBookDTO(String title, String isbn, String author, List<String> genres, int availableCopies, int totalCopies, LocalDateTime publishedDate) {
}
