package org.example.friendservice.domain.friend;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
  Optional<List<Friend>> findByUserId(Long userId);

  Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);
}
