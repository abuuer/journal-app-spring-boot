/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.ERole;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserRoleDetail;
import java.util.List;

/**
 *
 * @author anoir
 */
public interface UserRoleDetailService {

    int save(UserRoleDetail userRoleDetail);

    List<UserRoleDetail> findByUser_Email(String email);

    List<UserRoleDetail> findByRole_Name(ERole name);

    List<User> findAllReviewers();
    
    List<User> findAllAuthors();

}
