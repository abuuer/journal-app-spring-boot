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
import com.journal.journal.service.facade.UserRoleDetailService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserRoleDetailServiceImpl implements UserRoleDetailService {

    @Autowired
    private UserRoleDetailRepository userRoleDetailRepository;

    @Override
    public int save(UserRoleDetail userRoleDetail) {
        userRoleDetailRepository.save(userRoleDetail);
        return 1;
    }

    @Override
    public List<UserRoleDetail> findByUser_Id(Long id) {
        return userRoleDetailRepository.findByUser_Id(id);
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

}
