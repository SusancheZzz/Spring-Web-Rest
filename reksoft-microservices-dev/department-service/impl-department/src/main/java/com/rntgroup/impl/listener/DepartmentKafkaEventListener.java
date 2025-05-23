package com.rntgroup.impl.listener;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.listener.event.DepartmentMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentKafkaEventListener {

  private final KafkaTemplate<Long, DepartmentMessageDto> kafkaTemplate;

  @EventListener
  public void acceptEvent(DepartmentMessageEvent event) {
    var message = (DepartmentMessageDto) event.getSource();
    var topic = "department." + event.getOperation().name();

    kafkaTemplate.send(topic, message);
  }
}