package com.callcenter.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String extension;

    @Enumerated(EnumType.STRING)
    private AgentStatus status;
}