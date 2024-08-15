package com.ga5000.library.services;

import com.ga5000.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getBookByGenre(String genre);
    List<Book> getBooksByAuthor(String author);
    Book getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Book book, Long id);
    void deleteBook(Long id);


}
