package com.project.ChatEncryptedApp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.runtime.ObjectMethods;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor

public class Service {

    private final UserRepository userRepository;
    public ResponseEntity<Object> addUser(User x) {

        try {

            if (userRepository.findByUserID(x.getUserID()) == null) {
                userRepository.save(x);
                return new ResponseEntity<>("Successfully saved user \n \n " + x.toString(), HttpStatus.CREATED);
            }

            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {

            System.out.println(e.toString());
            return new ResponseEntity<>("Un Expected error has occurred", HttpStatus.BAD_REQUEST);

        }
    }

    public ResponseEntity<Object> findbyUserID(String userID) {
        User v = userRepository.findByUserID(userID);
        if (v == null) {
            return new ResponseEntity<>("User not exist", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(v, HttpStatus.OK);
        }

    }

    public User findbyUserIDUser(String userID) {

        User v = userRepository.findByUserID(userID);

            return v;

    }

    public ResponseEntity<Object> deletebyUserID(String userID) {
        User v = userRepository.findByUserID(userID);
        if (v != null) {
            userRepository.delete(v);
            return new ResponseEntity<>("User has deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not exist", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> getAllUsers() {
        List<User> list = userRepository.findAll();

        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    public List<User> getAllUsersList() {
        List<User> list = userRepository.findAll();

        return list;

    }
}
