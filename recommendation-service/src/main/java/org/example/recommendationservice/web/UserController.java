package org.example.recommendationservice.web;

import java.util.List;
import org.example.recommendationservice.domain.friend.FriendRepository;
import org.example.recommendationservice.domain.friend.entity.RankedUser;
import org.example.recommendationservice.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class UserController {
  private final FriendRepository repository;

  public UserController(FriendRepository repository) {
    this.repository = repository;
  }

  @GetMapping(path = "/users/{userId}/commands/findMutualFriends")
  @ResponseStatus(code = HttpStatus.OK)
  public List<User> getMutualFriends(@PathVariable Long userId, @RequestParam Long friendId) {
    return repository.mutualFriends(userId, friendId);
  }

  @GetMapping(path = "/users/{userId}/commands/recommendFriends")
  @ResponseStatus(code = HttpStatus.OK)
  public List<RankedUser> recommendFriends(@PathVariable Long userId) {
    return repository.recommendedFriends(userId);
  }
}
