package com.ga5000.library;

import com.ga5000.library.dtos.*;
import com.ga5000.library.exceptions.*;
import com.ga5000.library.model.*;
import com.ga5000.library.repositories.*;
import com.ga5000.library.services.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Book book;
    private Member member;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setBookId(1L);
        book.setAvailableCopies(10);

        member = new Member();
        member.setMemberId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setTransactionType(TransactionType.BORROW);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReturnDate(LocalDateTime.now().plusDays(7));
    }

    @Test
    void testCreateTransactionSuccess() {

        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(1L, 1L, TransactionType.BORROW, LocalDateTime.now());


        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));


        TransactionDTO result = transactionService.createTransaction(createTransactionDTO);


        assertNotNull(result);
        assertEquals(1L, result.bookId());
        assertEquals(1L, result.memberId());
        assertEquals(TransactionType.BORROW, result.transactionType());
        assertTrue(result.transactionDate().isAfter(LocalDateTime.now().minusSeconds(10))); // Check if transactionDate is recent
        assertTrue(result.returnDate().isAfter(result.transactionDate())); // Check if returnDate is after transactionDate

        verify(bookRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    void testCreateTransactionNoAvailableCopies() {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(1L, 1L, TransactionType.BORROW, LocalDateTime.now());
        book.setAvailableCopies(0);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        assertThrows(NoAvailableCopiesException.class, () -> transactionService.createTransaction(createTransactionDTO));
    }

    @Test
    void testGetTransactionByIdSuccess() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionDTO result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.bookId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void testGetTransactionsByMemberIdSuccess() {
        when(transactionRepository.findByMember_MemberId(1L)).thenReturn(List.of(transaction));

        List<TransactionDTO> result = transactionService.getTransactionsByMemberId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).bookId());
        verify(transactionRepository, times(1)).findByMember_MemberId(1L);
    }

    @Test
    void testGetTransactionsByMemberIdEmpty() {
        when(transactionRepository.findByMember_MemberId(1L)).thenReturn(List.of());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionsByMemberId(1L));
    }

    @Test
    void testRenewTransactionSuccess() {
        RenewTransactionDTO renewTransactionDTO = new RenewTransactionDTO(1L, 1L, TransactionType.RENEW);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionDTO result = transactionService.renew(1L, renewTransactionDTO);

        assertNotNull(result);
        assertEquals(1L, result.bookId());
        assertEquals(TransactionType.BORROW, result.transactionType());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testReturnBookSuccess() {
        ReturnTransactionDTO returnTransactionDTO = new ReturnTransactionDTO(1L, 1L, TransactionType.RETURN);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionDTO result = transactionService.returnBook(1L, returnTransactionDTO);

        assertNotNull(result);
        assertEquals(1L, result.bookId());
        verify(transactionRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testReturnBookNotFound() {
        ReturnTransactionDTO returnTransactionDTO = new ReturnTransactionDTO(1L, 1L, TransactionType.RETURN);

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.returnBook(1L, returnTransactionDTO));
    }

    @Test
    void testDeleteTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetCurrentBorrowedBooksSuccess() {
        when(transactionRepository.findByMember_MemberIdAndTransactionType(1L, TransactionType.BORROW)).thenReturn(List.of(transaction));
        when(bookRepository.findAllById(anyList())).thenReturn(List.of(book));

        List<Book> result = transactionService.getCurrentBorrowedBooks(1L);

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getBookId());
        verify(transactionRepository, times(1)).findByMember_MemberIdAndTransactionType(1L, TransactionType.BORROW);
        verify(bookRepository, times(1)).findAllById(anyList());
    }

    @Test
    void testGetTransactionsByDateRangeSuccess() {
        when(transactionRepository.findByTransactionDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(transaction));

        List<TransactionDTO> result = transactionService.getTransactionsByDateRange(LocalDateTime.now().minusDays(7), LocalDateTime.now());

        assertFalse(result.isEmpty());
        verify(transactionRepository, times(1)).findByTransactionDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

}
