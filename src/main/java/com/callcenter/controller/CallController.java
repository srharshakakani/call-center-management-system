package com.callcenter.controller;

import com.callcenter.model.Call;
import com.callcenter.model.CallStatus;
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

    @GetMapping("/queue")
    public List<Call> getQueue() {
        return service.getQueue();
    }

    @PatchMapping("/{id}")
    public Call updateCall(
            @PathVariable Long id,
            @RequestParam CallStatus status,
            @RequestParam(required = false) Long agentId) {

        return service.updateCall(id, status, agentId);
    }
}