package com.simulation.shop.repository;

import com.coffee.shared.entity.AuditLog;
import com.coffee.shared.entity.StepTransactionId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditLogRepository extends CrudRepository<AuditLog, StepTransactionId> {
}
