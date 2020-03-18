package com.sjr.controller;

import com.sjr.model.User;
import com.sjr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testBoot")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/selAll")
    @ResponseBody
    public List<User> selAll(){
        return userService.selAll();
    }


    @PostMapping("/saveAll")
    @ResponseBody
    public String saveAll(){
        return userService.saveAll();
    }

    @PutMapping("/updateByBatch")
    @ResponseBody
    public String updateByBatch(){
        return userService.updateByBatch();
    }
}
