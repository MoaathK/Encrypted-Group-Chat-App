package com.project.ChatEncryptedApp.Controller;


import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.Service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class Controller {

    private final Service service;

    @PostMapping("/adduser")
   public ResponseEntity<Object> addUser(@RequestBody User x) {
        return service.addUser(x);

    }

    @GetMapping("/findbyUserID/{id}")

    public ResponseEntity<Object> findbyUserID(@PathVariable ("id") String userId) {
        return service.findbyUserID(userId);
    }

    @GetMapping("/findbyUserIDUser/{id}")

    public User findbyUserIDUser(@PathVariable ("id") String userId) {

        return service.findbyUserIDUser(userId);

    }

    @DeleteMapping("/deletebyUserID/{id}")
    public ResponseEntity<Object> deletebyUserID(@PathVariable ("id") String userID) {
        return service.deletebyUserID(userID);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Object> getAllUsers() {
        return service.getAllUsers();
    }
    @GetMapping("/getAllUsersList")
    public List<User> getAllUsersList() {
        return service.getAllUsersList();
    }



}
