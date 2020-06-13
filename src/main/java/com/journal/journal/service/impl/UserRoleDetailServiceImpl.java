/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.dao.UserRoleDetailRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.UserRoleDetailService;
import com.journal.journal.service.facade.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserRoleDetailServiceImpl implements UserRoleDetailService {

    @Autowired
    private UserRoleDetailRepository userRoleDetailRepository;

    @Autowired
    private UserService userService;
    
    @Override
    public int save(UserRoleDetail userRoleDetail) {
        userRoleDetailRepository.save(userRoleDetail);
        return 1;
    }


    @Override
    public List<UserRoleDetail> findByRole_Name(ERole name) {
        return userRoleDetailRepository.findByRole_Name(name);
    }

    @Override
    public List<User> findAllReviewers() {
        List<UserRoleDetail> urd = findByRole_Name(ERole.ROLE_REVIEWER);
        if(urd == null){
            return null;
        } else {
            List<User> users = new ArrayList<>();
            urd.forEach((userRoleDetail) -> {
                users.add(userRoleDetail.getUser());
            });
            return users;
        }
        
    }

    @Override 
    public List<User> findAllAuthors() {
        List<UserRoleDetail> urd = findByRole_Name(ERole.ROLE_AUTHOR);
        if(urd == null){
            return null;
        } else {
            List<User> users = new ArrayList<>();
            urd.forEach((userRoleDetail) -> {
                users.add(userRoleDetail.getUser());
            });
            return users;
        }
    }

    @Override
    public List<UserRoleDetail> findByUser_Email(String email) {
        return userRoleDetailRepository.findByUser_Email(email);
    }

    @Override
    public ResponseEntity<?> deleteByUser_Email(String email) {
        Optional<User> fUser = userService.findByEmail(email);
        if (!fUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User doesn't exist"));
        } else {
            userRoleDetailRepository.deleteByUser_Email(email);
            return ResponseEntity.ok(new MessageResponse(fUser.get().getFirstName() + " "
                    + fUser.get().getLastName() + " is dismissed"));
        }
    }

    @Override
    public void delete(UserRoleDetail uerRoleDetail) {
        userRoleDetailRepository.delete(uerRoleDetail);
    }

}
