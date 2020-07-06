package org.example.friendservice.domain.friend;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.friendservice.domain.user.UserClient;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class FriendService {
  private final FriendRepository repository;
  private final UserClient userClient;
  private final Source messageBroker;

  public FriendService(FriendRepository repository, UserClient userClient, Source messageBroker) {
    this.repository = repository;
    this.userClient = userClient;
    this.messageBroker = messageBroker;
  }

  @Transactional
  public Friend create(Friend friend) {
    try {
      // The userId and friendId must not be the same, as users can not be friend themselves
      if (friend.getFriendId().equals(friend.getUserId())) {
        throw new HttpClientErrorException(
            HttpStatus.BAD_REQUEST, "A user can not be friend one self");
      }

      // Check whether user id and friend id are valid
      userClient.getUser(friend.getFriendId());
      userClient.getUser(friend.getUserId());

      // Save friend
      final Friend entity = repository.saveAndFlush(friend);

      // Set the entity payload after it has been updated in the database, but before committed
      FriendEvent event = new FriendEvent(entity, EventType.FRIEND_ADDED);
      // Attempt to perform a reactive dual-write to message broker by sending a domain event
      messageBroker.output().send(MessageBuilder.withPayload(event).build(), 30000L);
      // The application dual-write was a success and the database transaction can commit
      return entity;
    } catch (HttpClientErrorException e) {
      log.error("A dual-write transaction to the message broker has failed: {}", friend);
      // This error will cause the database transaction to be rolled back
      throw new HttpClientErrorException(
          HttpStatus.INTERNAL_SERVER_ERROR, "A transactional error occurred");
    }
  }

  @Transactional
  public Friend delete(Friend friend) {
    try {
      // Check for friends relationship, if doesn't exist, fail it
      final Friend entity =
          repository
              .findByUserIdAndFriendId(friend.getUserId(), friend.getFriendId())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend Not Found"));

      // Delete friend
      repository.delete(friend);
      repository.flush();

      // Set the entity payload after it has been updated in the database, but before committed
      FriendEvent event = new FriendEvent(entity, EventType.FRIEND_REMOVED);
      // Attempt to perform a reactive dual-write to message broker by sending a domain event
      messageBroker.output().send(MessageBuilder.withPayload(event).build(), 30000L);
      // The application dual-write was a success and the database transaction can commit
      return entity;
    } catch (HttpClientErrorException e) {
      log.error("A dual-write transaction to the message broker has failed: {}", friend);
      // This error will cause the database transaction to be rolled back
      throw new HttpClientErrorException(
          HttpStatus.INTERNAL_SERVER_ERROR, "A transactional error occurred");
    }
  }

  @Transactional(readOnly = true)
  public Optional<Friend> find(Long id) {
    return repository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<List<Friend>> findUserFriends(Long userId) {
    return repository.findByUserId(userId);
  }
}
