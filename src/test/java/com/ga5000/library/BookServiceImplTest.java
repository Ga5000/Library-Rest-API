package com.ga5000.library;

import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.model.Book;
import com.ga5000.library.repositories.BookRepository;
import com.ga5000.library.services.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookByGenre_BooksFound() {
        Book book = new Book();
        List<Book> books = List.of(book);
        when(bookRepository.findByGenre("Fiction")).thenReturn(books);

        List<Book> result = bookService.getBookByGenre("Fiction");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void testGetBookByGenre_BooksNotFound() {
        when(bookRepository.findByGenre("Non-Fiction")).thenReturn(List.of());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookByGenre("Non-Fiction");
        });

        assertEquals("Books with genre: Non-Fiction were not found", thrown.getMessage());
    }

    @Test
    void testGetBooksByAuthor_BooksFound() {
        Book book = new Book();
        List<Book> books = List.of(book);
        when(bookRepository.findByAuthor("Author")).thenReturn(books);

        List<Book> result = bookService.getBooksByAuthor("Author");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book, result.get(0));
    }

    @Test
    void testGetBooksByAuthor_BooksNotFound() {
        when(bookRepository.findByAuthor("Unknown Author")).thenReturn(List.of());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBooksByAuthor("Unknown Author");
        });

        assertEquals("Books with genre: Unknown Author were not found", thrown.getMessage());
    }

    @Test
    void testGetBookById_BookFound() {
        Book book = new Book();
        when(bookRepository.findByIdWithComments(anyLong())).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    void testGetBookById_BookNotFound() {
        when(bookRepository.findByIdWithComments(anyLong())).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book with id: 1 wasn't found", thrown.getMessage());
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(book);

        assertNotNull(result);
        assertEquals(book, result);
    }

    @Test
    void testUpdateBook() {
        Book existingBook = new Book();
        Book updatedBook = new Book();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Book result = bookService.updateBook(updatedBook, 1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBook_BookNotFound() {
        Book updatedBook = new Book();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(updatedBook, 1L);
        });

        assertEquals("Book with id: 1 wasn't found", thrown.getMessage());
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book with id: 1 wasn't found", thrown.getMessage());
    }
}

