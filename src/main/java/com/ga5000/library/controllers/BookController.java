package com.ga5000.library.controllers;


import com.ga5000.library.dtos.Book.BookDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookController {
    ResponseEntity<BookDTO> createBook(BookDTO book);
    ResponseEntity<BookDTO> updateBook(Long bookId, BookDTO book);
    ResponseEntity<Void> deleteBook(Long bookId);
    ResponseEntity<List<BookDTO>> getBooksByGenre(String genre);
    ResponseEntity<List<BookDTO>> getBooksByAuthor(String author);
    ResponseEntity<BookDTO> getBookWithComments(Long bookId);
}
