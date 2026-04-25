package com.callcenter.service;

import com.callcenter.dto.SummaryResponse;
import com.callcenter.model.*;
import com.callcenter.repository.AgentRepository;
import com.callcenter.repository.CallRepository;
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

    public Call create(Call call) {
        return callRepository.save(call);
    }

    public List<Call> getAll() {
        return callRepository.findAll();
    }

    public List<Call> getQueue() {
        return callRepository.getQueue();
    }

    // Find least-loaded AVAILABLE agent
    public Agent findLeastLoadedAgent() {
        List<Agent> agents = agentRepository.findAll();

        Agent best = null;
        long minLoad = Long.MAX_VALUE;

        for (Agent agent : agents) {
            if (agent.getStatus() != AgentStatus.AVAILABLE) continue;

            long load = callRepository.countByAssignedAgentIdAndStatus(
                    agent.getId(), CallStatus.IN_PROGRESS
            );

            if (load < minLoad) {
                minLoad = load;
                best = agent;
            }
        }

        return best;
    }

    @Transactional
    public Call updateCall(Long id, CallStatus status, Long agentId) {

        Call call = callRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Call not found"));

        // Cannot modify resolved call
        if (call.getStatus() == CallStatus.RESOLVED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot modify resolved call");
        }

        Agent agent = null;

        // If agent explicitly provided
        if (agentId != null) {
            agent = agentRepository.findById(agentId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Agent not found"));

            if (agent.getStatus() != AgentStatus.AVAILABLE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Agent not available");
            }
        }
        // Auto-assign least-loaded agent
        else if (status == CallStatus.IN_PROGRESS) {
            agent = findLeastLoadedAgent();

            if (agent == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No available agents");
            }
        }

        // Assign agent + mark BUSY
        if (agent != null) {
            call.setAssignedAgent(agent);
            agent.setStatus(AgentStatus.BUSY);
            agentRepository.save(agent); // IMPORTANT FIX
        }

        // IN_PROGRESS requires agent
        if (status == CallStatus.IN_PROGRESS && call.getAssignedAgent() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "IN_PROGRESS requires assigned agent");
        }

        // Release agent when resolved
        if (status == CallStatus.RESOLVED && call.getAssignedAgent() != null) {
            Agent assigned = call.getAssignedAgent();
            assigned.setStatus(AgentStatus.AVAILABLE);
            agentRepository.save(assigned);
        }

        call.setStatus(status);

        return callRepository.save(call);
    }

    // Dashboard summary
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
}