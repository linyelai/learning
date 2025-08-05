package com.linyelai.controller;

import com.linyelai.controller.request.AddFriendRequest;
import com.linyelai.controller.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: linyelai
 * date: 2025/8/5
 * description: 好有管理
 */
@RestController
public class FriendController {

    /**
     *
     * @return
     */
    @PostMapping("/addFriend")
    public Response addFriend(@RequestBody AddFriendRequest request){
        Response response = new Response();

        return response;
    }
}
