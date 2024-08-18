package com.ga5000.library.services;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.dtos.Book.BookWithCommentsDTO;
import com.ga5000.library.dtos.Book.CreateBookDTO;
import com.ga5000.library.dtos.Book.UpdateBookDTO;

import java.util.List;


public interface BookService {

    List<BookDTO> getBookByGenre(String genre);
    List<BookDTO> getBooksByAuthor(String author);
    BookWithCommentsDTO getBookById(Long id);
    BookDTO createBook(CreateBookDTO book);
    BookDTO updateBook(UpdateBookDTO book, Long id);
    void deleteBook(Long id);

}
