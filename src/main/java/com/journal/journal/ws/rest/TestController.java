/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/journal-api/test")
public class TestController {

    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;

    @GetMapping("/id/{id}")
    public List<Tag> findTagByUserId(@PathVariable Long id) {
        System.out.println(id);
        return userSpecialtyDetailService.findTagByUserId(id);
    }

    
    
    
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('AUTHOR') or hasRole('EDITOR') or hasRole('REVIEWER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/author")
    @PreAuthorize("hasRole('AUTHOR')")
    public String authorAccess() {
        return "Author Board.";
    }

    @GetMapping("/editor")
    @PreAuthorize("hasRole('EDITOR')")
    public String editorAccess() {
        return "Editor Board.";
    }

    @GetMapping("/reviewer")
    @PreAuthorize("hasRole('REVIEWER')")
    public String reviewerAccess() {
        return "REVIEWER Board.";
    }
}
