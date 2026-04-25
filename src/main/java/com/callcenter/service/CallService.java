package com.callcenter.service;

import com.callcenter.dto.SummaryResponse;
import com.callcenter.model.*;
import com.callcenter.repository.CallRepository;
import com.callcenter.repository.AgentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CallService {

    private final CallRepository callRepository;
    private final AgentRepository agentRepository;

    public CallService(CallRepository callRepository, AgentRepository agentRepository) {
        this.callRepository = callRepository;
        this.agentRepository = agentRepository;
    }

    public SummaryResponse getSummary() {

        long total = callRepository.count();

        long queued = callRepository.countByStatus(CallStatus.QUEUED);
        long inProgress = callRepository.countByStatus(CallStatus.IN_PROGRESS);
        long resolved = callRepository.countByStatus(CallStatus.RESOLVED);
        long escalated = callRepository.countByStatus(CallStatus.ESCALATED);

        long availableAgents = agentRepository.countByStatus(AgentStatus.AVAILABLE);
        long busyAgents = agentRepository.countByStatus(AgentStatus.BUSY);

        return new SummaryResponse(
                total,
                queued,
                inProgress,
                resolved,
                escalated,
                availableAgents,
                busyAgents
        );
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

    @Transactional
    public Call updateCall(Long id, CallStatus status, Long agentId) {

        //  Call not found → 404
        Call call = callRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Call not found"));

        // Cannot modify resolved
        if (call.getStatus() == CallStatus.RESOLVED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot modify resolved call");
        }

        // Assign agent if provided
        if (agentId != null) {
            Agent agent = agentRepository.findById(agentId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Agent not found"));

            //  Agent not available
            if (agent.getStatus() != AgentStatus.AVAILABLE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Agent not available");
            }

            call.setAssignedAgent(agent);
            agent.setStatus(AgentStatus.BUSY);
        }

        //  IN_PROGRESS requires agent
        if (status == CallStatus.IN_PROGRESS && call.getAssignedAgent() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "IN_PROGRESS requires assigned agent");
        }

        call.setStatus(status);

        return call;
    }
}