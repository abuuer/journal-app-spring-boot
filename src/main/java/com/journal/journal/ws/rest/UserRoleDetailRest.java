/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.User;
import com.journal.journal.service.facade.UserRoleDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user-role")
public class UserRoleDetailRest {

    @Autowired
    private UserRoleDetailService userRoleDetailService;

    @GetMapping("/findAllReviewers")
    public List<User> findAllReviewers() {
        return userRoleDetailService.findAllReviewers();
    }

    @GetMapping("/findAllAuthors")
    public List<User> findAllAuthors() {
        return userRoleDetailService.findAllAuthors();
    }

}
