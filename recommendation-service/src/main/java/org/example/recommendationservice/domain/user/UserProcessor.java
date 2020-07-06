package org.example.recommendationservice.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

/**
 * Message stream listener for {@link User} events. Maps types of events to a graph operation that
 * replicates a connected view of domain data across microservices.
 */
@Configuration
@EnableBinding(UserSink.class)
@Slf4j
public class UserProcessor {
  private final UserRepository repository;

  public UserProcessor(UserRepository repository) {
    this.repository = repository;
  }

  @StreamListener(value = UserSink.INPUT)
  @Transactional
  public void apply(Message<UserEvent> userEvent) {
    log.info("Event received: {}", userEvent.getPayload().toString());

    switch (userEvent.getPayload().getEventType()) {
      case USER_CREATED:
        final User subject = userEvent.getPayload().getSubject();
        User newUser = repository.save(subject);
        log.info("Created user: {}", newUser);
        break;
      case USER_UPDATED:
        User updateUser = userEvent.getPayload().getSubject();
        User findUser = repository.findUserByUserId(updateUser.getId());
        if (findUser != null) {
          findUser.toBuilder()
              .createdAt(updateUser.getCreatedAt())
              .updatedAt(updateUser.getUpdatedAt())
              .firstName(updateUser.getFirstName())
              .lastName(updateUser.getLastName())
              .build();
          findUser = repository.save(findUser);
          log.info("Updated user: {}", findUser.toString());
        }
        break;
      default:
        break;
    }
  }
}
