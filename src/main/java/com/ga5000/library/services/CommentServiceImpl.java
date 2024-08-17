package com.ga5000.library.services;

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
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

import java.util.List;

public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public CommentDTO createComment(Long bookId, Long memberId, CreateCommentDTO createCommentDTO) {
        Comment comment = new Comment();
        Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException("Book with id: "+bookId+" wasn't found"));
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("Member with id: "+memberId+" wasn't found"));
        BeanUtils.copyProperties(createCommentDTO,comment);
        comment.setBook(book);
        comment.setMember(member);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        return toCommentDTO(comment);
    }

    @Transactional
    @Override
    public CommentDTO updateComment(Long commentId, UpdateCommentDTO updateCommentDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: "+commentId+" wasn't found"));
        existingComment.setContent(updateCommentDTO.content());
        commentRepository.save(existingComment);

        return toCommentDTO(existingComment);

    }

    @Override
    public List<CommentDTO> getCommentsOfMemberByMemberId(Long memberId) {
        List<Comment> comments = commentRepository.findByMember_MemberId(memberId);
        if (comments.isEmpty()) {
            throw new MemberNotFoundException("No comments found for member with id: " + memberId);
        }
        return comments.stream()
                .map(this::toCommentDTO).toList();
    }

    @Override
    public List<CommentDTO> getBookCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBook_BookId(bookId);
        if (comments.isEmpty()) {
            throw new BookNotFoundException("No comments found for book with id: " + bookId);
        }
        return comments.stream()
                .map(this::toCommentDTO).toList();
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: "+commentId+" wasn't found"));

        commentRepository.deleteById(commentId);
    }

    private CommentDTO toCommentDTO(Comment comment){
        return new CommentDTO(
                comment.getMember().getUsername(),
                comment.getCreatedAt(),
                comment.getContent()
        );
    }
}
