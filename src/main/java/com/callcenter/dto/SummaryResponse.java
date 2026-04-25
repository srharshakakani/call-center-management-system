package com.callcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryResponse {

    private long totalCalls;
    private long queued;
    private long inProgress;
    private long resolved;
    private long escalated;

    private long availableAgents;
    private long busyAgents;
}