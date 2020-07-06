package org.example.recommendationservice.domain.friend;

import java.time.LocalDateTime;
import java.util.List;
import org.example.recommendationservice.domain.friend.entity.Friend;
import org.example.recommendationservice.domain.friend.entity.RankedUser;
import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/** Repository for managing {@link User} data and friend connections */
@Repository
public interface FriendRepository extends Neo4jRepository<Friend, Long> {
  @Query(
      "MATCH (userA:User), (userB:User) "
          + "WHERE userA.userId=$0 AND userB.userId=$1 "
          + "CREATE (userA)-[:FRIEND { createdAt: $2, updatedAt: $3 }]->(userB)")
  void addFriend(Long fromId, Long toId, LocalDateTime createdAt, LocalDateTime updatedAt);

  @Query(
      "MATCH (userA:User)-[r:FRIEND]->(userB:User) "
          + "WHERE userA.userId=$0 AND userB.userId=$1 "
          + "DELETE r")
  void removeFriend(Long fromId, Long toId);

  @Query(
      "MATCH (userA:User), (userB:User) "
          + "WHERE userA.userId=$0 AND userB.userId=$1 "
          + "MATCH (userA)-[:FRIEND]-(fof:User)-[:FRIEND]-(userB) "
          + "RETURN DISTINCT fof")
  List<User> mutualFriends(Long fromId, Long toId);

  @Query(
      "MATCH (me:User {userId: $0})-[:FRIEND]-(friends), "
          + "(nonFriend:User)-[:FRIEND]-(friends) "
          + "WHERE NOT (me)-[:FRIEND]-(nonFriend) "
          + "WITH nonFriend, count(nonFriend) as mutualFriends "
          + "RETURN nonFriend as User, mutualFriends as Weight "
          + "ORDER BY Weight DESC")
  List<RankedUser> recommendedFriends(Long userId);
}
