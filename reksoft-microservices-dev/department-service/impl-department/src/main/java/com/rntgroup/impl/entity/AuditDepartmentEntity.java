package com.rntgroup.impl.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.rntgroup.impl.listener.event.AuditObjectEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_department", schema = "audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditDepartmentEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Long departmentId;

  private Instant timestamp;

  @Enumerated(EnumType.STRING)
  private Operation operation;

  public AuditDepartmentEntity(Long departmentId, Instant timestamp, Operation operation) {
    this.departmentId = departmentId;
    this.timestamp = timestamp;
    this.operation = operation;
  }

  public AuditDepartmentEntity(AuditObjectEvent auditObjectEvent) {
    this.departmentId = (Long) auditObjectEvent.getSource();
    this.timestamp = auditObjectEvent.getTimestamp();
    this.operation = auditObjectEvent.getOperation();
  }
}
