package com.ga5000.library;

import com.ga5000.library.controllers.BookControllerImpl;
import com.ga5000.library.dtos.Book.BookDTO;
import com.ga5000.library.services.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(BookControllerImpl.class)
class BookControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp() {
        List<String> genres = Arrays.asList("Fiction", "Adventure");
        mockBookDTO = new BookDTO("Test Title", "123-456-789", "Test Author", genres, 10, 10, new Date());
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        when(bookService.createBook(any(BookDTO.class))).thenReturn(mockBookDTO);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(mockBookDTO)));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        Long bookId = 1L;
        when(bookService.updateBook(any(BookDTO.class), any(Long.class))).thenReturn(mockBookDTO);

        mockMvc.perform(put("/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockBookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockBookDTO)));
    }

    @Test
    void deleteBook_ShouldReturnStatusOk() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{bookId}", bookId))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksByGenre_ShouldReturnBooks() throws Exception {
        List<BookDTO> books = Collections.singletonList(mockBookDTO);
        when(bookService.getBookByGenre("Fiction")).thenReturn(books);

        mockMvc.perform(get("/books/genre/{genre}", "Fiction"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    void getBooksByAuthor_ShouldReturnBooks() throws Exception {
        List<BookDTO> books = Collections.singletonList(mockBookDTO);
        when(bookService.getBooksByAuthor("Test Author")).thenReturn(books);

        mockMvc.perform(get("/books/author/{author}", "Test Author"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    void getBookWithComments_ShouldReturnBook() throws Exception {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(mockBookDTO);

        mockMvc.perform(get("/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockBookDTO)));
    }
}
