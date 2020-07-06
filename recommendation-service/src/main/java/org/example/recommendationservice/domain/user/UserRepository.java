package org.example.recommendationservice.domain.user;

import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
  User findUserByUserId(Long userId);
}
