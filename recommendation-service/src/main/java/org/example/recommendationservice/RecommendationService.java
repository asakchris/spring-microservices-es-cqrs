package org.example.recommendationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories(
    value = {
      "org.example.recommendationservice.domain.friend",
      "org.example.recommendationservice.domain.user"
    })
@EntityScan({
  "org.example.recommendationservice.domain.friend.entity",
  "org.example.recommendationservice.domain.user.entity"
})
public class RecommendationService {
  public static void main(String[] args) {
    SpringApplication.run(RecommendationService.class, args);
  }
}
