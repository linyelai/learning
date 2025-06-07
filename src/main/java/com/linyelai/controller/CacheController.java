package com.linyelai.controller;

import com.linyelai.controller.request.CacheRequest;
import com.linyelai.controller.response.Response;
import com.linyelai.service.impl.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("cache")
public class CacheController {
    @Autowired
    private CacheService cacheService;
    @PostMapping("/set")
    public Response setCache(@RequestBody CacheRequest cacheRequest){

        cacheService.setCache(cacheRequest.getKey(),cacheRequest.getValue(),5, TimeUnit.MINUTES);
        return new Response();
    }
    @GetMapping("/get")
    public Response getCache(@RequestParam("key")String key){
        Response response = new Response();
        String value = (String)cacheService.getCache(key);
        response.setBody(value);
        return response;
    }
}
