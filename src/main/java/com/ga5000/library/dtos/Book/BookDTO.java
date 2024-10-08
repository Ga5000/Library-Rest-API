package com.ga5000.library.dtos.Book;

import java.util.Date;
import java.util.List;

public record BookDTO(Long id, String title, String isbn, String author, List<String> genres, int availableCopies, int totalCopies, Date publishedDate) {
}
