/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.User;
import com.journal.journal.security.payload.request.LoginRequest;
import com.journal.journal.security.payload.request.SignupRequest;
import com.journal.journal.service.facade.UserService;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user")
public class UserRest {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @GetMapping("/email/{email}")
    public Optional<User> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PutMapping("/authorToReviewer/email/{email}")
    public ResponseEntity<?> authorToReviewer(@PathVariable String email) {
        return userService.authorToReviewer(email);
    }

    @PutMapping("/confirmUser/email/{email]/password/{password}")
    public ResponseEntity<?> confirmUser(String email, String password) {
        return userService.confirmUser(email, password);
    }

    @PutMapping("/confirmRegistraion/token/{token}/password/{password}")
    public ResponseEntity<?> confirmRegistraion(@PathVariable String token,@PathVariable String password) {
        return userService.confirmRegistraion(token,password);
    }

    @Transactional
    @DeleteMapping("/deleteAccount/email/{email}")
    public ResponseEntity<?> deleteAccount(@PathVariable String email) {
        return userService.deleteAccount(email);
    }

    @DeleteMapping("/dismissReviewer/email/{email}")
    @Transactional
    public ResponseEntity<?> dismissReviewer(@PathVariable String email) {
        return userService.dismissReviewer(email);
    }
    
    
    
    
    
}
