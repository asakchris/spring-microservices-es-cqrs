package org.example.recommendationservice.domain.friend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * Get a list of {@link User} ranked by a weight. This model is used to get a ranked number of my
 * friend's mutual friends who I am not yet friends with. This is a typical recommendation query you
 * would find on most social networks.
 */
@QueryResult
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class RankedUser {
  private User user;
  private Integer weight;
}
