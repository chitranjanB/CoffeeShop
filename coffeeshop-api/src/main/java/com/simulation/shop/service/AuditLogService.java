package com.simulation.shop.service;

import com.simulation.shop.entity.AuditLog;
import com.simulation.shop.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    public void audit(AuditLog auditLog) {
        repository.save(auditLog);
    }
}
