package com.simulation.shop.repository;

import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.entity.StepTransactionId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditLogRepository extends CrudRepository<AuditLog, StepTransactionId> {
}
