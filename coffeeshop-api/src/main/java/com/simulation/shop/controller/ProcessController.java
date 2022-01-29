package com.simulation.shop.controller;

import com.simulation.shop.request.OrderRequest;
import com.simulation.shop.request.OrderResponse;
import com.simulation.shop.service.Order;
import com.simulation.shop.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @PostMapping
    public OrderResponse process(OrderRequest request) {
        String transactionId = processService.buildTransactionId();
        List<Order> orderList = processService.queueOrder(request);

        //TODO for each transactionId
        for (Order order : orderList) {
            //TODO grind -> brew -> steam
            // call using resttemplate
            //TODO archive the order
        }
        //TODO return response as downloadable zip
        return null;
    }
}
