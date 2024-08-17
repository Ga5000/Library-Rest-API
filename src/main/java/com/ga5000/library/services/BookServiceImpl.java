package com.ga5000.library.services;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.exceptions.InvalidIsbnException;
import com.ga5000.library.model.Book;
import com.ga5000.library.repositories.BookRepository;
import com.ga5000.library.services.components.IsbnValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final IsbnValidator isbnValidator;

    public BookServiceImpl(BookRepository bookRepository, IsbnValidator isbnValidator) {
        this.bookRepository = bookRepository;
        this.isbnValidator = isbnValidator;
    }

    @Override
    public List<BookDTO> getBookByGenre(String genre) {
        List<Book> books = bookRepository.findByGenre(genre);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Books with genre: " + genre + " were not found");
        }
        return books.stream()
                .map(this::toBookDTO).toList();
    }

    @Override
    public List<BookDTO> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Books with author: " + author + " were not found");
        }
        return books.stream()
                .map(this::toBookDTO).toList();
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findByIdWithComments(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " wasn't found"));
        return toBookDTO(book);
    }

    @Transactional
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        if (!isbnValidator.isValidIsbn(book.getIsbn())) {
            throw new InvalidIsbnException("This book ISBN: " + book.getIsbn() + " is not valid");
        }
        bookRepository.save(book);
        return toBookDTO(book);
    }

    @Transactional
    @Override
    public BookDTO updateBook(BookDTO bookDTO, Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " wasn't found"));
        BeanUtils.copyProperties(bookDTO, existingBook);

        if (!isbnValidator.isValidIsbn(existingBook.getIsbn())) {
            throw new InvalidIsbnException("This book ISBN: " + existingBook.getIsbn() + " is not valid");
        }

        bookRepository.save(existingBook);
        return toBookDTO(existingBook);
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " wasn't found"));

        bookRepository.deleteById(id);
    }

    private BookDTO toBookDTO(Book book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor(),
                book.getGenres(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getPublishedDate()
        );
    }

}
