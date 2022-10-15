package com.simulation.shop.controller;

import com.coffee.shared.model.Transaction;
import com.simulation.shop.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService service;

    @GetMapping
    public List<Transaction> findTransactionsByStatus(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestParam String status) {
        return service.findTransactionsByStatus(status);
    }

    @GetMapping("/all/ids")
    public List<String> fetchTransactionIds(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth) {
        return service.fetchTransactionIds();
    }

}
