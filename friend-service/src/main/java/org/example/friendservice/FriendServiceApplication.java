package org.example.friendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableFeignClients
public class FriendServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(FriendServiceApplication.class, args);
  }

  @Configuration
  @EnableBinding(Source.class)
  class StreamConfig {}
}
