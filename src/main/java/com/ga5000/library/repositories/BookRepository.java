package com.ga5000.library.repositories;

import com.ga5000.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT b FROM Book b WHERE :genre MEMBER OF b.genres ORDER BY b.title ASC")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM Book b WHERE b.author = :author ORDER BY b.title ASC")
    List<Book> findByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.comments WHERE b.id = :id")
    Optional<Book> findByIdWithComments(@Param("id") Long id);
}
