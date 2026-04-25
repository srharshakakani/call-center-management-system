package com.callcenter.repository;

import com.callcenter.model.Agent;
import com.callcenter.model.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    long countByStatus(AgentStatus status);
}