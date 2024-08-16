package com.ga5000.library.services;

import com.ga5000.library.dtos.Transaction.CreateTransactionDTO;
import com.ga5000.library.dtos.Transaction.RenewTransactionDTO;
import com.ga5000.library.dtos.Transaction.ReturnTransactionDTO;
import com.ga5000.library.dtos.Transaction.TransactionDTO;
import com.ga5000.library.exceptions.*;
import com.ga5000.library.model.Book;
import com.ga5000.library.model.Member;
import com.ga5000.library.model.Transaction;
import com.ga5000.library.model.TransactionType;
import com.ga5000.library.repositories.BookRepository;
import com.ga5000.library.repositories.MemberRepository;
import com.ga5000.library.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public TransactionDTO createTransaction(CreateTransactionDTO createTransactionDTO) {
        Book book = bookRepository.findById(createTransactionDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + createTransactionDTO.bookId() + " wasn't found"));


        Member member = memberRepository.findById(createTransactionDTO.memberId())
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + createTransactionDTO.memberId() + " wasn't found"));

        if (createTransactionDTO.transactionType() == TransactionType.BORROW) {
            if (book.getAvailableCopies() <= 0) {
                throw new NoAvailableCopiesException("No available copies for book with id: " + createTransactionDTO.bookId());
            }
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        } else {
            throw new TransactionTypeException("Transaction type must be BORROW for new transactions");
        }

        Transaction transaction = new Transaction();

        BeanUtils.copyProperties(createTransactionDTO, transaction);
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setTransactionType(TransactionType.BORROW);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReturnDate(transaction.getTransactionDate().plusDays(7));

        transactionRepository.save(transaction);
        return toTransactionDTO(transaction);
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        return findByIdAndConvertToDTO(id,this::toTransactionDTO);
    }

    @Override
    public List<TransactionDTO> getTransactionsByMemberId(Long memberId) {
       List<Transaction> transactions = transactionRepository.findByMember_MemberId(memberId);
       if(transactions.isEmpty()){
           throw new TransactionNotFoundException("This member has not made any transactions");
       }
        return transactions.stream().map(this::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByBookId(Long bookId) {
        List<Transaction> transactions = transactionRepository.findByBook_BookId(bookId);
        if(transactions.isEmpty()){
            throw new TransactionNotFoundException("This book doesn't have any transactions");
        }
         return transactions.stream().map(this::toTransactionDTO).toList();
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll(Sort.by(Sort.Direction.ASC))
                .stream().map(this::toTransactionDTO).toList();
    }

    @Transactional
    @Override
    public TransactionDTO renew(Long id, RenewTransactionDTO renewTransactionDTO) {
        Transaction transaction = findById(id);
        Book book = bookRepository.findById(renewTransactionDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + renewTransactionDTO.bookId() + " wasn't found"));

        Member member = memberRepository.findById(renewTransactionDTO.memberId())
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + renewTransactionDTO.memberId() + " wasn't found"));

        if(transaction.getTransactionType() != TransactionType.BORROW){
            throw new TransactionTypeException("Cannot renew a transaction that is not a BORROW type.");
        }
        if(renewTransactionDTO.transactionType() == TransactionType.RENEW){
            transaction.setReturnDate(transaction.getReturnDate().plusDays(7));
        }
            transactionRepository.save(transaction);
            return toTransactionDTO(transaction);
    }

    @Transactional
    @Override
    public TransactionDTO returnBook(Long id, ReturnTransactionDTO returnTransactionDTO) {
        Transaction transaction = findById(id);
        Book book = bookRepository.findById(returnTransactionDTO.bookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + returnTransactionDTO.bookId() + " wasn't found"));

        Member member = memberRepository.findById(returnTransactionDTO.memberId())
                .orElseThrow(() -> new MemberNotFoundException("Member with id: " + returnTransactionDTO.memberId() + " wasn't found"));

        if(transaction.getTransactionType() != TransactionType.BORROW){
            throw new TransactionTypeException("Cannot return a transaction that is not a BORROW type.");
        }
        if(returnTransactionDTO.transactionType() == TransactionType.RETURN){
            transaction.setReturnDate(null);
            book.setAvailableCopies(book.getAvailableCopies()+1);
        }
            transaction.setBook(book);
            transaction.setMember(member);
            transactionRepository.save(transaction);
            return toTransactionDTO(transaction);

    }
    @Transactional
    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id: "+id+" wasn't found"));

        transactionRepository.deleteById(id);
    }

    @Override
    public boolean isValidTransaction(Long id) {
        return false;
    }

    @Override
    public List<Book> getCurrentBorrowedBooks(Long memberId) {

        List<Transaction> transactions = transactionRepository.findByMember_MemberIdAndTransactionType(memberId, TransactionType.BORROW);

        List<Long> bookIds = transactions.stream()
                .map(transaction -> transaction.getBook().getBookId())
                .distinct()
                .toList();

        return bookRepository.findAllById(bookIds);

    }

    @Override
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        
        return transactions.stream()
                .map(this::toTransactionDTO)
                .toList();
    }


    public TransactionDTO findByIdAndConvertToDTO(Long id, Function<Transaction, TransactionDTO> converter) {
        Transaction transaction = findById(id);
        return converter.apply(transaction);
    }

    private Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id: "+id+" wasn't found"));
    }

    private TransactionDTO toTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getBook().getBookId(),
                transaction.getMember().getMemberId(),
                transaction.getTransactionType(),
                transaction.getTransactionDate(),
                transaction.getReturnDate()
        );
    }

}
