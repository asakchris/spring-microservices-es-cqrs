package org.example.friendservice.domain.friend;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Setter
@Getter
@ToString
public class FriendEvent extends DomainEvent<Friend> {
  private Friend subject;
  private EventType eventType;
  private FriendMessage friendMessage;

  public FriendEvent(EventType friendRemoved) {}

  public FriendEvent(Friend subject, EventType eventType) {
    this.subject = subject;
    this.eventType = eventType;
  }

  public FriendEvent(Friend subject, EventType eventType, FriendMessage friendMessage) {
    this.subject = subject;
    this.eventType = eventType;
    this.friendMessage = friendMessage;
  }

  public FriendEvent(
      Friend subject, Friend subject1, EventType eventType, FriendMessage friendMessage) {
    super(subject);
    this.subject = subject1;
    this.eventType = eventType;
    this.friendMessage = friendMessage;
  }
}
