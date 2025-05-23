package com.rntgroup.impl.listener;

import com.rntgroup.impl.entity.AuditDepartmentEntity;
import com.rntgroup.impl.listener.event.AuditObjectEvent;
import com.rntgroup.impl.repository.AuditDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DepartmentAuditEventListener {

  private final AuditDepartmentRepository auditDepartmentRepository;

  @EventListener
  @Transactional
  public void acceptEvent(AuditObjectEvent auditObjectEvent) {
    var auditDepartmentEntity = new AuditDepartmentEntity(auditObjectEvent);
    auditDepartmentRepository.save(auditDepartmentEntity);
  }
}
