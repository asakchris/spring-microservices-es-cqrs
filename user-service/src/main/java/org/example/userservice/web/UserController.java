package org.example.userservice.web;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.example.userservice.domain.User;
import org.example.userservice.domain.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping(path = "/users")
  @ResponseStatus(code = HttpStatus.CREATED)
  public User createUser(@NotNull(message = "User must not equal null") @RequestBody User user) {
    return service.create(user);
  }

  @PutMapping(path = "/users/{userId}")
  @ResponseStatus(code = HttpStatus.OK)
  public User updateUser(
      @NotNull(message = "UserId must not equal null") @PathVariable Long userId,
      @NotNull(message = "User must not equal null") @RequestBody User user) {
    if (!userId.equals(user.getUserId())) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "The userId supplied in the URI path does not match the payload");
    }
    return service.update(user);
  }

  @GetMapping(path = "/users/{userId}")
  @ResponseStatus(code = HttpStatus.OK)
  public User getUser(@PathVariable("userId") Long userId) {
    return service
        .find(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
  }

  @GetMapping(path = "/users")
  @ResponseStatus(code = HttpStatus.OK)
  public List<User> getAllUsers() {
    return service.findAll();
  }
}
