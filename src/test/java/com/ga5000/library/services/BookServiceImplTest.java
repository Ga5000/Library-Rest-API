package com.ga5000.library.services;

import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.dtos.Book.BookWithCommentsDTO;
import com.ga5000.library.dtos.Book.CreateBookDTO;
import com.ga5000.library.dtos.Book.UpdateBookDTO;
import com.ga5000.library.exceptions.BookExistsException;
import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.model.Book;
import com.ga5000.library.model.Comment;
import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.BookRepository;
import com.ga5000.library.services.components.IsbnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private IsbnValidator isbnValidator;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookByGenre_ShouldReturnBooks_WhenBooksExist() {
        // Arrange
        Book book = new Book(); // Create mock book
        book.setTitle("Test Book");
        when(bookRepository.findByGenre("Fiction")).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.getBookByGenre("Fiction");

        // Assert
        assertFalse(result.isEmpty());
        assertEquals("Test Book", result.get(0).title());
        verify(bookRepository, times(1)).findByGenre("Fiction");
    }

    @Test
    void getBookByGenre_ShouldThrowException_WhenNoBooksExist() {
        when(bookRepository.findByGenre("Nonexistent")).thenReturn(List.of());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookByGenre("Nonexistent"));

        verify(bookRepository, times(1)).findByGenre("Nonexistent");
    }

    @Test
    void createBook_ShouldThrowException_WhenBookExistsByIsbn() {
        CreateBookDTO createBookDTO = new CreateBookDTO("Test", "12345", "Author", List.of("Fiction"), 5, 10, null);

        when(bookRepository.existsByIsbn("12345")).thenReturn(true);

        assertThrows(BookExistsException.class, () -> bookService.createBook(createBookDTO));

        verify(bookRepository, times(1)).existsByIsbn("12345");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void createBook_ShouldCreateBook_WhenValidIsbnAndDoesNotExist() {
        CreateBookDTO createBookDTO = new CreateBookDTO("Test", "12345", "Author", List.of("Fiction"), 5, 10, null);
        Book book = new Book();

        when(bookRepository.existsByIsbn("12345")).thenReturn(false);
        when(isbnValidator.isValidIsbn("12345")).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.createBook(createBookDTO);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExistsAndValidIsbn() {
        UpdateBookDTO updateBookDTO = new UpdateBookDTO("Updated Title", "Updated Author", List.of("Updated Genre"), 5, 10, null);
        Book book = new Book();
        book.setIsbn("12345");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(isbnValidator.isValidIsbn("12345")).thenReturn(true);

        BookDTO result = bookService.updateBook(updateBookDTO, 1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenBookExists() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void getBookById_ShouldReturnBookWithComments_WhenBookExists() {
        Book book = new Book();
        book.setTitle("Test Book");

        Member member = new Member();
        member.setUsername("Gabriel");

        Comment comment = new Comment();
        comment.setContent("aaaaa");
        comment.setMember(member);
        comment.setCreatedAt(Date.from(Instant.now()));

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        book.setComments(comments);

        when(bookRepository.findByIdWithComments(1L)).thenReturn(Optional.of(book));

        BookWithCommentsDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.title());
        verify(bookRepository, times(1)).findByIdWithComments(1L);
    }
}
