package com.example.workflowautomation.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shipmentId;

    private String shipName;
    private String origin;
    private String destination;
    private String productType;
    private int quantity;
    private int weight;
    private String status;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String clientName;
    private String priority;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}
