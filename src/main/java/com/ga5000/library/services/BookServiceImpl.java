package com.ga5000.library.services;

import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.model.Book;
import com.ga5000.library.repositories.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getBookByGenre(String genre) {
        List<Book> books = bookRepository.findByGenre(genre);
        if(books.isEmpty()){
            throw  new BookNotFoundException("Books with genre: "+genre+" were not found");
        }
            return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books =  bookRepository.findByAuthor(author);
        if(books.isEmpty()){
            throw  new BookNotFoundException("Books with genre: "+author+" were not found");
        }
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findByIdWithComments(id)
                .orElseThrow(() -> new BookNotFoundException(("Book with id: "+id+" wasn't found")));
    }

    @Transactional
    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(Book book, Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: "+id+" wasn't found"));
        BeanUtils.copyProperties(book,existingBook);

        return bookRepository.save(existingBook);
    }

    @Transactional
    @Override
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: "+id+" wasn't found"));

        bookRepository.deleteById(id);
    }

}
