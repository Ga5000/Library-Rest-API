package com.ga5000.library.services;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.model.Book;

import java.util.List;


public interface BookService {

    List<BookDTO> getBookByGenre(String genre);
    List<BookDTO> getBooksByAuthor(String author);
    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO book);
    BookDTO updateBook(BookDTO book, Long id);
    void deleteBook(Long id);



}
