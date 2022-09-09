package com.simulation.shop.repository;

import com.coffee.shared.entity.AuditLog;
import com.coffee.shared.entity.StepTransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, StepTransactionId> {
}
