package org.example.recommendationservice.domain.friend.entity;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.recommendationservice.domain.user.entity.User;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

@RelationshipEntity("FRIEND")
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Friend {
  @Id @GeneratedValue @EqualsAndHashCode.Include private Long id;
  @StartNode @EqualsAndHashCode.Include private User user;
  @EndNode @EqualsAndHashCode.Include private User friend;
  @DateLong private LocalDateTime createdAt;
  @DateLong private LocalDateTime lastModified;

  public Friend() {}

  public Friend(User user, User friend) {
    this.user = user;
    this.friend = friend;
  }
}
