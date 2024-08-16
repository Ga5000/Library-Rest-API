package com.ga5000.library.controllers;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.services.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookControllerImpl implements BookController {
    private final BookServiceImpl bookService;

    public BookControllerImpl(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Override
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(book));
    }

    @PutMapping("/{bookId}")
    @Override
    public ResponseEntity<BookDTO> updateBook(@PathVariable("bookId") Long bookId, @RequestBody @Valid BookDTO book) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(book, bookId));
    }

    @DeleteMapping("/{bookId}")
    @Override
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/genre/{genre}")
    @Override
    public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable("genre") String genre) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByGenre(genre));
    }

    @GetMapping("/author/{author}")
    @Override
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable("author") String author) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/{bookId}")
    @Override
    public ResponseEntity<BookDTO> getBookWithComments(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookById(bookId));
    }
}
