package com.simulation.shop.repository;

import com.simulation.shop.entity.GrinderAuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<GrinderAuditLog, String> {
}
