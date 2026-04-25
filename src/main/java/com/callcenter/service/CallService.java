package com.callcenter.service;

import com.callcenter.model.Call;
import com.callcenter.repository.CallRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallService {

    private final CallRepository callRepository;

    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public Call create(Call call) {
        return callRepository.save(call);
    }

    public List<Call> getAll() {
        return callRepository.findAll();
    }

    public List<Call> getQueue() {
        return callRepository.getQueue();
    }
}