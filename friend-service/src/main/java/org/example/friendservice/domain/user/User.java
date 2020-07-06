package org.example.friendservice.domain.user;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User {
  private Long userId;
  private String firstName;
  private String lastName;
  private LocalDateTime createdAt;
  private LocalDateTime lastModified;

  public User() {}

  public User(String firstName, String lastName) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(Long id, String firstName, String lastName) {
    this.userId = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(
      Long userId,
      String firstName,
      String lastName,
      LocalDateTime createdAt,
      LocalDateTime lastModified) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.createdAt = createdAt;
    this.lastModified = lastModified;
  }
}
