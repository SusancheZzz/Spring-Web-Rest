package com.rntgroup.impl.listener.event;

import java.util.EventObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentMessageEvent extends EventObject {

  public enum MessageOperationType {
    MODIFYING, DELETE
  }

  private MessageOperationType operation;

  public DepartmentMessageEvent(Object source, MessageOperationType operation) {
    super(source);
    this.operation = operation;
  }
}