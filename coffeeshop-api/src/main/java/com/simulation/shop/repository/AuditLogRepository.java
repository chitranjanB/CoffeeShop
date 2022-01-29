package com.simulation.shop.repository;

import com.simulation.shop.entity.AuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, String> {
}
