package com.simulation.shop.controller;

import com.coffee.shared.model.Coffee;
import com.coffee.shared.request.TransactionRequest;
import com.simulation.shop.service.EspressoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/machine")
@CrossOrigin(origins = "http://localhost:3000")
public class EspressoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EspressoController.class);

    @Autowired
    private EspressoService service;

    @PostMapping(value = "/espresso")
    public Coffee makeEspresso(@RequestHeader(value = HttpHeaders.AUTHORIZATION, defaultValue = "") String auth, @RequestBody TransactionRequest request) {
        return service.makeEspresso(request.getTransactionId());
    }
}
