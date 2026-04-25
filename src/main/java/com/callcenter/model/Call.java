package com.callcenter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String callerName;

    @NotBlank
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CallStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String notes;

    @ManyToOne
    private Agent assignedAgent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = CallStatus.QUEUED;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}