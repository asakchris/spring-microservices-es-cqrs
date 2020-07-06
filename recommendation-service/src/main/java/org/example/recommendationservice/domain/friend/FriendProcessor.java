package org.example.recommendationservice.domain.friend;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.example.recommendationservice.domain.user.UserRepository;
import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableBinding(FriendSink.class)
@Transactional
@Slf4j
public class FriendProcessor {
  private final FriendRepository friendRepository;
  private final UserRepository userRepository;

  public FriendProcessor(FriendRepository friendRepository, UserRepository userRepository) {
    this.friendRepository = friendRepository;
    this.userRepository = userRepository;
  }

  @StreamListener(value = FriendSink.INPUT)
  public void apply(Message<FriendEvent> friendEvent) {
    log.info("Event received: {}", friendEvent.toString());

    User user = userRepository.findUserByUserId(friendEvent.getPayload().getSubject().getUserId());
    User friend =
        userRepository.findUserByUserId(friendEvent.getPayload().getSubject().getFriendId());

    if (user == null || friend == null) {
      throw new RuntimeException(
          "Invalid user identifier for "
              + friendEvent.getPayload().getEventType()
              + " operation on one or more users: "
              + Arrays.asList(user, friend).toString());
    }

    switch (friendEvent.getPayload().getEventType()) {
      case FRIEND_ADDED:
        friendRepository.addFriend(
            user.getUserId(),
            friend.getUserId(),
            friendEvent.getPayload().getSubject().getCreatedAt(),
            friendEvent.getPayload().getSubject().getUpdatedAt());
        break;
      case FRIEND_REMOVED:
        friendRepository.removeFriend(user.getId(), friend.getId());
        break;
    }
  }
}
