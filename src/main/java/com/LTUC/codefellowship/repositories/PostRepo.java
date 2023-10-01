package com.LTUC.codefellowship.repositories;

import com.LTUC.codefellowship.models.ApplicationUser;
import com.LTUC.codefellowship.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findByUserId(ApplicationUser applicationUser);

    List<Post> findByUserIdIn(Set<ApplicationUser> following);
}
