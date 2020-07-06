package org.example.friendservice.web;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.example.friendservice.domain.friend.Friend;
import org.example.friendservice.domain.friend.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1")
public class FriendController {
  private final FriendService service;

  public FriendController(FriendService service) {
    this.service = service;
  }

  @GetMapping(path = "/friends/{id}")
  @ResponseStatus(code = HttpStatus.OK)
  public Friend getFriend(@PathVariable Long id) {
    return service
        .find(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend Not Found"));
  }

  @GetMapping(path = "/users/{userId}/friends")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Friend> getFriends(@PathVariable Long userId) {
    return service
        .findUserFriends(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend Not Found"));
  }

  @PostMapping(path = "/users/{userId}/commands/addFriend")
  @ResponseStatus(code = HttpStatus.CREATED)
  public Friend addFriend(
      @NotNull(message = "UserId must not equal null") @PathVariable Long userId,
      @NotNull(message = "FriendId must not equal null") @RequestParam("friendId") Long friendId) {
    Friend friend = Friend.builder().userId(userId).friendId(friendId).build();
    return service.create(friend);
  }

  @DeleteMapping(path = "/users/{userId}/commands/removeFriend")
  @ResponseStatus(code = HttpStatus.CREATED)
  public Friend removeFriend(
      @NotNull(message = "UserId must not equal null") @PathVariable Long userId,
      @NotNull(message = "FriendId must not equal null") @RequestParam("friendId") Long friendId) {
    Friend friend = Friend.builder().userId(userId).friendId(friendId).build();
    return service.delete(friend);
  }
}
