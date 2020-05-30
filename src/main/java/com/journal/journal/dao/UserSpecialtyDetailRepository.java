/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.User;
import com.journal.journal.bean.UserSpecialtyDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author anoir
 */
@Repository
public interface UserSpecialtyDetailRepository extends JpaRepository<UserSpecialtyDetail, Long> {

    List<UserSpecialtyDetail> findByUser(User user);

}
