package com.simulation.shop.controller;

import com.simulation.shop.model.Coffee;
import com.simulation.shop.model.Grounds;
import com.simulation.shop.model.SteamedMilk;
import com.simulation.shop.request.InputRequest;
import com.simulation.shop.request.OrderRequest;
import com.simulation.shop.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public List<String> process(@RequestBody OrderRequest request) {
        List<String> orderList = processService.queueOrder(request);

        for (String transactionId : orderList) {
            InputRequest inputRequest = new InputRequest();
            inputRequest.setTransactionId(transactionId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<InputRequest> requestEntity = new HttpEntity<InputRequest>(inputRequest, headers);

            ResponseEntity<Grounds> groundsResponse = restTemplate.postForEntity("http://localhost:8080/machine/grind", inputRequest, Grounds.class);
            ResponseEntity<Coffee> espressoResponse = restTemplate.postForEntity("http://localhost:8080/machine/espresso", inputRequest, Coffee.class);
            ResponseEntity<SteamedMilk> steamedResponse = restTemplate.postForEntity("http://localhost:8080/machine/steam", inputRequest, SteamedMilk.class);

            processService.archiveOrder(transactionId);
        }
        //TODO return response as downloadable zip
        return orderList;
    }
}
