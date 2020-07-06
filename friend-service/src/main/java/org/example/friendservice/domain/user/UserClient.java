package org.example.friendservice.domain.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "userClient", url = "${user.service.url}")
public interface UserClient {
  @GetMapping("/users/{userId}")
  User getUser(@PathVariable Long userId);
}
