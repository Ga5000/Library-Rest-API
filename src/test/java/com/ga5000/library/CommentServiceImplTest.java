package com.ga5000.library;

import com.ga5000.library.dtos.Comment.CommentDTO;
import com.ga5000.library.dtos.Comment.CreateCommentDTO;
import com.ga5000.library.dtos.Comment.UpdateCommentDTO;
import com.ga5000.library.exceptions.BookNotFoundException;
import com.ga5000.library.exceptions.CommentNotFoundException;
import com.ga5000.library.exceptions.MemberNotFoundException;
import com.ga5000.library.model.Book;
import com.ga5000.library.model.Comment;
import com.ga5000.library.model.Member;
import com.ga5000.library.repositories.BookRepository;
import com.ga5000.library.repositories.CommentRepository;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.services.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComment_Success() {
        Long bookId = 1L;
        Long memberId = 1L;
        CreateCommentDTO createCommentDTO = new CreateCommentDTO("Content");

        Book book = new Book();
        Member member = new Member();
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setMember(member);
        comment.setCreatedAt(Date.from(Instant.now()));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.createComment(bookId, memberId, createCommentDTO);

        assertNotNull(result);
        assertEquals("Content", result.content());
    }


    @Test
    void testCreateComment_BookNotFound() {
        Long bookId = 1L;
        Long memberId = 1L;
        CreateCommentDTO createCommentDTO = new CreateCommentDTO("Content");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            commentService.createComment(bookId, memberId, createCommentDTO);
        });

        assertEquals("Book with id: 1 wasn't found", thrown.getMessage());
    }

    @Test
    void testCreateComment_MemberNotFound() {
        Long bookId = 1L;
        Long memberId = 1L;
        CreateCommentDTO createCommentDTO = new CreateCommentDTO("Content");

        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        MemberNotFoundException thrown = assertThrows(MemberNotFoundException.class, () -> {
            commentService.createComment(bookId, memberId, createCommentDTO);
        });

        assertEquals("Member with id: 1 wasn't found", thrown.getMessage());
    }

    @Test
    void testUpdateComment_Success() {
        Long commentId = 1L;
        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO("Updated content");

        Member member = new Member();
        member.setUserName("username");

        Comment existingComment = new Comment();
        existingComment.setContent("Old content");
        existingComment.setMember(member);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        CommentDTO result = commentService.updateComment(commentId, updateCommentDTO);

        assertNotNull(result);
        assertEquals("Updated content", result.content());
    }

    @Test
    void testUpdateComment_CommentNotFound() {
        Long commentId = 1L;
        UpdateCommentDTO updateCommentDTO = new UpdateCommentDTO("Updated content");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentNotFoundException thrown = assertThrows(CommentNotFoundException.class, () -> {
            commentService.updateComment(commentId, updateCommentDTO);
        });

        assertEquals("Comment with id: 1 wasn't found", thrown.getMessage());
    }

    @Test
    void testGetCommentsOfMemberByMemberId_Success() {
        Long memberId = 1L;
        Comment comment = new Comment();
        comment.setContent("Content");
        comment.setCreatedAt(new Date());
        comment.setMember(new Member() {{
            setUsername("username");
        }});

        List<Comment> comments = List.of(comment);
        when(commentRepository.findByMemberId(memberId)).thenReturn(comments);

        List<CommentDTO> result = commentService.getCommentsOfMemberByMemberId(memberId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("username", result.get(0).member());
    }

    @Test
    void testGetCommentsOfMemberByMemberId_NoCommentsFound() {
        Long memberId = 1L;
        when(commentRepository.findByMemberId(memberId)).thenReturn(List.of());

        MemberNotFoundException thrown = assertThrows(MemberNotFoundException.class, () -> {
            commentService.getCommentsOfMemberByMemberId(memberId);
        });

        assertEquals("No comments found for member with id: 1", thrown.getMessage());
    }

    @Test
    void testGetBookCommentsByBookId_Success() {
        Long bookId = 1L;
        Comment comment = new Comment();
        comment.setContent("Content");
        comment.setCreatedAt(new Date());
        comment.setMember(new Member() {{
            setUsername("username");
        }});

        List<Comment> comments = List.of(comment);
        when(commentRepository.findByBookId(bookId)).thenReturn(comments);

        List<CommentDTO> result = commentService.getBookCommentsByBookId(bookId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("username", result.get(0).member());
    }

    @Test
    void testGetBookCommentsByBookId_NoCommentsFound() {
        Long bookId = 1L;
        when(commentRepository.findByBookId(bookId)).thenReturn(List.of());

        BookNotFoundException thrown = assertThrows(BookNotFoundException.class, () -> {
            commentService.getBookCommentsByBookId(bookId);
        });

        assertEquals("No comments found for book with id: 1", thrown.getMessage());
    }

    @Test
    void testDeleteComment_Success() {
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testDeleteComment_CommentNotFound() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentNotFoundException thrown = assertThrows(CommentNotFoundException.class, () -> {
            commentService.deleteComment(commentId);
        });

        assertEquals("Comment with id: 1 wasn't found", thrown.getMessage());
    }
}
