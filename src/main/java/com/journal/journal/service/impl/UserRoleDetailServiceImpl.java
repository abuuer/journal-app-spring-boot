/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.UserRoleDetail;
import com.journal.journal.dao.UserRoleDetailRepository;
import com.journal.journal.service.facade.UserRoleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserRoleDetailServiceImpl implements UserRoleDetailService{

    @Autowired
    private UserRoleDetailRepository userRoleDetailRepository;
    
    @Override
    public int save(UserRoleDetail userRoleDetail) {
         userRoleDetailRepository.save(userRoleDetail);
         return 1;
    }
    
}
