package com.example.workflowautomation.repository;


import com.example.workflowautomation.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;



public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentId(String shipmentId);

    List<Shipment> findByLastUpdatedAfter(LocalDateTime time);
}
