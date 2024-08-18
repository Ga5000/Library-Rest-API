package com.ga5000.library.controllers;


import com.ga5000.library.dtos.Book.BookDTO;

import com.ga5000.library.dtos.Book.BookWithCommentsDTO;
import com.ga5000.library.dtos.Book.CreateBookDTO;
import com.ga5000.library.dtos.Book.UpdateBookDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookController {
    ResponseEntity<BookDTO> createBook(CreateBookDTO book);
    ResponseEntity<BookDTO> updateBook(Long bookId, UpdateBookDTO book);
    ResponseEntity<Void> deleteBook(Long bookId);
    ResponseEntity<List<BookDTO>> getBooksByGenre(String genre);
    ResponseEntity<List<BookDTO>> getBooksByAuthor(String author);
    ResponseEntity<BookWithCommentsDTO> getBookWithComments(Long bookId);
}
