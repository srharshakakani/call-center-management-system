package com.callcenter.controller;

import com.callcenter.model.Agent;
import com.callcenter.repository.AgentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentRepository agentRepository;

    public AgentController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @PostMapping
    public Agent create(@RequestBody Agent agent) {
        return agentRepository.save(agent);
    }

    @GetMapping
    public List<Agent> getAll() {
        return agentRepository.findAll();
    }
}