package com.callcenter.controller;

import com.callcenter.dto.SummaryResponse;
import com.callcenter.service.CallService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final CallService service;

    public DashboardController(CallService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public SummaryResponse getSummary() {
        return service.getSummary();
    }
}