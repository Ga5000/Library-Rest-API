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
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;

import java.util.Date;

@Service
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
    public CommentDTO createComment(Long bookId, Long memberId, CreateCommentDTO createCommentDTO, String currentUsername) throws AccessDeniedException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + bookId + " wasn't found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + memberId + " wasn't found"));


        if (member.getUsername().equals(currentUsername)) {

            Comment comment = new Comment();
            BeanUtils.copyProperties(createCommentDTO, comment);

            comment.setBook(book);
            comment.setMember(member);
            comment.setCreatedAt(Date.from(Instant.now()));

            commentRepository.save(comment);

            return toCommentDTO(comment);
        } else {
            throw new AccessDeniedException("You are not authorized to create a comment for this member");
        }
    }


    @Transactional
    @Override
    public CommentDTO updateComment(Long commentId, UpdateCommentDTO updateCommentDTO, String currentUsername) throws AccessDeniedException {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: " + commentId + " wasn't found"));

        if (existingComment.getMember().getUsername().equals(currentUsername)) {

            existingComment.setContent(updateCommentDTO.content());

            commentRepository.save(existingComment);

            return toCommentDTO(existingComment);
        } else {
            throw new AccessDeniedException("You are not authorized to update this comment");
        }
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
