package org.example.userservice.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Setter
@Getter
@ToString
public class UserEvent extends DomainEvent<User, Integer> {
  private User subject;
  private EventType eventType;

  public UserEvent() {
    this.setId(UUID.randomUUID().hashCode());
  }

  public UserEvent(User subject, EventType eventType) {
    this();
    this.subject = subject;
    this.eventType = eventType;
  }

  public UserEvent(EventType userCreated) {
    this.eventType = userCreated;
  }
}
