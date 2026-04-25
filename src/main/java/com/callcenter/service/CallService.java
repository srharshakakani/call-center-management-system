package com.callcenter.service;

import com.callcenter.model.Call;
import com.callcenter.repository.CallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallService {

    private final CallRepository callRepository;

    public Call create(Call call) {
        return callRepository.save(call);
    }

    public List<Call> getAll() {
        return callRepository.findAll();
    }
}