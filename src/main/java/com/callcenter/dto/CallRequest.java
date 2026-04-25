package com.callcenter.dto;

import lombok.Data;

@Data
public class CallRequest {
    private String callerName;
    private String phoneNumber;
    private String priority;
    private String notes;
}