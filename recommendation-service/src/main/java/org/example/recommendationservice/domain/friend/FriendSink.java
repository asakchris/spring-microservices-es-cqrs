package org.example.recommendationservice.domain.friend;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface FriendSink {
  String INPUT = "friend";

  @Input(FriendSink.INPUT)
  SubscribableChannel friend();
}
