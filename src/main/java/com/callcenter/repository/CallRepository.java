package com.callcenter.repository;

import com.callcenter.model.Call;
import com.callcenter.model.CallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {

    @Query("""
           SELECT c FROM Call c
           WHERE c.status = 'QUEUED'
           ORDER BY 
             CASE c.priority
               WHEN 'CRITICAL' THEN 1
               WHEN 'HIGH' THEN 2
               WHEN 'MEDIUM' THEN 3
               WHEN 'LOW' THEN 4
             END,
             c.createdAt ASC
           """)
    List<Call> getQueue();

    long countByAssignedAgentIdAndStatus(Long agentId, CallStatus status);

    long countByStatus(CallStatus status);
}