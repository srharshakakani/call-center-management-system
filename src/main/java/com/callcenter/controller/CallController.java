package com.callcenter.controller;

import com.callcenter.model.Call;
import com.callcenter.service.CallService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calls")
public class CallController {

    private final CallService service;

    public CallController(CallService service) {
        this.service = service;
    }

    @PostMapping
    public Call create(@RequestBody Call call) {
        return service.create(call);
    }

    @GetMapping
    public List<Call> getAll() {
        return service.getAll();
    }
}