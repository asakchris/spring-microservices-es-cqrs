package org.example.userservice.domain;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A domain event is an abstract class that describes a behavior within a domain.
 *
 * @param <T> is the type of domain object that this event applies to.
 * @param <ID> is the type of identity for the domain event.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public abstract class DomainEvent<T, ID> implements Serializable {
  private ID id;
  private Long createdAt;
  private Long lastModified;

  public abstract T getSubject();

  public abstract void setSubject(T subject);

  public abstract EventType getEventType();

  public abstract void setEventType(EventType eventType);
}
