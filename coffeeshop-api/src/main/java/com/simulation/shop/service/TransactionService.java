package com.simulation.shop.service;

import com.coffee.shared.entity.TransactionSequence;
import com.coffee.shared.model.Status;
import com.coffee.shared.model.Transaction;
import com.simulation.shop.repository.TransactionSequenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionSequenceRepository transactionSequenceRepository;

    public List<Transaction> findTransactionsByStatus(String status) {
        Status statusEnum = Status.valueOf(status);
        List<TransactionSequence> transactionSequences = transactionSequenceRepository.findByStatus(statusEnum);
        List<Transaction> transactions = transactionSequences.stream().map(t -> {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(t.getTransactionId());
            transaction.setStatus(t.getStatus());
            transaction.setCustomerId(t.getCustomerId());
            transaction.setTimeStarted(t.getTimeStarted());
            transaction.setTimeEnded(t.getTimeEnded());
            return transaction;
        }).collect(Collectors.toList());
        return transactions;
    }

    public List<String> fetchTransactionIds() {
        Iterable<TransactionSequence> all = transactionSequenceRepository.findAll();
        List<String> transactions = Streamable.of(all).stream().map(a -> a.getTransactionId()).collect(Collectors.toList());
        return transactions;
    }

}
