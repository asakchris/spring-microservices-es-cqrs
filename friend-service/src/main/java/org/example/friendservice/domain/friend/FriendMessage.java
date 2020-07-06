package org.example.friendservice.domain.friend;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@Setter
@Getter
@ToString
public class FriendMessage extends DomainEvent<FriendMessage> {
  private Long userId;
  private Long friendId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public FriendMessage() {}

  public FriendMessage(Long userId, Long friendId) {
    this.userId = userId;
    this.friendId = friendId;
  }

  public FriendMessage(
      Long userId, Long friendId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.userId = userId;
    this.friendId = friendId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
