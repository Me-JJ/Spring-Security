package com.Week5.SpringSecurity.utils;

import com.Week5.SpringSecurity.dto.PostDTO;
import com.Week5.SpringSecurity.entities.User;
import com.Week5.SpringSecurity.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostSecurity
{
    private final PostService postService;

    public boolean isOwnerOfPost(Long postId)
    {
        PostDTO postDTO = postService.getPostById(postId);
        User postAuthor= postDTO.getAuthor();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return postAuthor.getId().equals(user.getId());
    }
}
