package org.example.recommendationservice.domain.user.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

/** A projection of the {@link User} domain object that is owned by the user service. */
@NodeEntity
@Setter
@Getter
@Builder(toBuilder = true)
@ToString
public class User {
  @Id @GeneratedValue private Long id;
  private Long userId;
  private String firstName;
  private String lastName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public User() {}

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(Long userId, String firstName, String lastName) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(
      Long userId,
      String firstName,
      String lastName,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public User(
      Long id,
      Long userId,
      String firstName,
      String lastName,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
