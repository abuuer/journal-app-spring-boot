/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.User;
import java.util.Optional;

/**
 *
 * @author anoir
 */
public interface UserService {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    int save(User user);
    
}
