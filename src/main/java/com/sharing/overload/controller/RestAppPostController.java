package com.sharing.overload.controller;

import com.sharing.overload.entity.AppPost;
import com.sharing.overload.service.AppPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/posts")
public class RestAppPostController {

    @Autowired
    private AppPostService appPostService;

    @GetMapping("/{username}")
    public List<AppPost> getUserPosts(@PathVariable String username) {
        return appPostService.findAppPostByUsername(username);
    }

    @GetMapping("/id/{id}")
    public AppPost getPostById(@PathVariable long id) {
        return appPostService.findAppPostById(id);
    }
}
