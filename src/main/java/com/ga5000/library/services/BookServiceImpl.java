package com.ga5000.library.services;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.dtos.Book.BookWithCommentsDTO;
import com.ga5000.library.dtos.Book.CreateBookDTO;
import com.ga5000.library.dtos.Book.UpdateBookDTO;
import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.exceptions.BookExistsException;
import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.exceptions.InvalidIsbnException;
import com.ga5000.library.model.Book;
import com.ga5000.library.model.Comment;
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
    public BookWithCommentsDTO getBookById(Long id) {
        Book book = bookRepository.findByIdWithComments(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " wasn't found"));
        return toBookWithCommentsDTO(book);
    }

    @Transactional
    @Override
    public BookDTO createBook(CreateBookDTO createBookDTO) {

        if (bookRepository.existsByIsbn(createBookDTO.isbn())) {
            throw new BookExistsException("A book with ISBN: " + createBookDTO.isbn() + " already exists in the library.");
        }

        Book book = new Book();
        BeanUtils.copyProperties(createBookDTO, book);

        if (!isbnValidator.isValidIsbn(book.getIsbn())) {
            throw new InvalidIsbnException("The ISBN: " + book.getIsbn() + " is not valid.");
        }

        bookRepository.save(book);

        return toBookDTO(book);
    }

    @Transactional
    @Override
    public BookDTO updateBook(UpdateBookDTO updateBookDTO, Long id) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " wasn't found"));

        BeanUtils.copyProperties(updateBookDTO, existingBook);

        if (!isbnValidator.isValidIsbn(existingBook.getIsbn())) {
            throw new InvalidIsbnException("The ISBN: " + existingBook.getIsbn() + " is not valid.");
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

    private BookWithCommentsDTO toBookWithCommentsDTO(Book book) {
        return new BookWithCommentsDTO(
                book.getBookId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthor(),
                book.getGenres(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getPublishedDate(),
                book.getComments().stream().map(this::toCommentDTO).toList()
        );
    }

    private CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getMember().getUsername(),
                comment.getCreatedAt(),
                comment.getContent()
        );

    }
}
