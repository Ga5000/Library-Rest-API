package com.ga5000.library.dtos.Book;

import java.util.Date;
import java.util.List;

public record UpdateBookDTO(String title,String author, List<String> genres, int availableCopies, int totalCopies, Date publishedDate) {
}
