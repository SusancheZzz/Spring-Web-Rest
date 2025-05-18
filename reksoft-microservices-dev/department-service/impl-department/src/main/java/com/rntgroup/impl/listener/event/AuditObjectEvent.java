package com.rntgroup.impl.listener.event;

import com.rntgroup.impl.entity.Operation;
import java.time.Instant;
import java.util.EventObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditObjectEvent extends EventObject {

  private final Instant timestamp;
  private final Operation operation;

  public AuditObjectEvent(Object source, Instant timestamp, Operation operation) {
    super(source);
    this.timestamp = timestamp;
    this.operation = operation;
  }
}
