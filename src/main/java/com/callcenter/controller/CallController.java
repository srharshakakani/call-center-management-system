package com.callcenter.controller;

import com.callcenter.dto.CallRequest;
import com.callcenter.model.Call;
import com.callcenter.model.CallStatus;
import com.callcenter.model.Priority;
import com.callcenter.service.CallService;
import jakarta.validation.Valid;
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
    public Call create(@Valid @RequestBody CallRequest req) {
        Call call = new Call();
        call.setCallerName(req.getCallerName());
        call.setPhoneNumber(req.getPhoneNumber());
        call.setPriority(Priority.valueOf(req.getPriority()));
        call.setNotes(req.getNotes());

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

    @PostMapping("/{id}/update")
    public Call updateCall(
            @PathVariable Long id,
            @RequestParam CallStatus status,
            @RequestParam(required = false) Long agentId) {

        return service.updateCall(id, status, agentId);
    }
}