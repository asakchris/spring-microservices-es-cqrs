package org.example.userservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserService {
  private final UserRepository repository;
  private final Source messageBroker;

  public UserService(UserRepository repository, Source messageBroker) {
    this.repository = repository;
    this.messageBroker = messageBroker;
  }

  @Transactional
  public User create(User user) {
    try {
      final User entity = repository.saveAndFlush(user);
      // Set the entity payload after it has been updated in the database, but before being
      // committed
      UserEvent event = new UserEvent(entity, EventType.USER_CREATED);
      log.info("Generating USER_CREATED event with payload: {}", event);
      // Attempt to perform a reactive dual-write to message broker by sending a domain event
      messageBroker.output().send(MessageBuilder.withPayload(event).build(), 30000L);
      return entity;
      // The application dual-write was a success and the database transaction can commit
    } catch (Exception exception) {
      log.error("A dual-write transaction to the message broker has failed: {}", user);
      // This error will cause the database transaction to be rolled back
      throw new HttpClientErrorException(
          HttpStatus.INTERNAL_SERVER_ERROR, "A transactional error occurred");
    }
  }

  @Transactional
  public User update(User user) {
    try {
      // Check user already exists or not, if doesn't exist, fail it
      final User entity =
          repository
              .findById(user.getUserId())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

      if (user.getFirstName().equals(entity.getFirstName())
          && user.getLastName().equals(entity.getLastName())) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No change to existing values");
      }

      // Update User
      entity.setFirstName(user.getFirstName());
      entity.setLastName(user.getLastName());
      entity.setUpdatedAt(LocalDateTime.now());
      repository.saveAndFlush(entity);

      // Set the entity payload after it has been updated in the database, but before being
      // committed
      UserEvent event = new UserEvent(entity, EventType.USER_UPDATED);
      log.info("Generating USER_CREATED event with payload: {}", event);
      // Attempt to perform a reactive dual-write to message broker by sending a domain event
      messageBroker.output().send(MessageBuilder.withPayload(event).build(), 30000L);
      return entity;
      // The application dual-write was a success and the database transaction can commit
    } catch (Exception exception) {
      log.error("A dual-write transaction to the message broker has failed: {}", user);
      // This error will cause the database transaction to be rolled back
      throw new HttpClientErrorException(
          HttpStatus.INTERNAL_SERVER_ERROR, "A transactional error occurred");
    }
  }

  @Transactional(readOnly = true)
  public Optional<User> find(Long userId) {
    return repository.findById(userId);
  }

  @Transactional(readOnly = true)
  public List<User> findAll() {
    return repository.findAll();
  }
}
