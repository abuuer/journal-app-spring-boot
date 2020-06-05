/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Tag;
import com.journal.journal.bean.UserSpecialtyDetail;
import java.util.List;

/**
 *
 * @author anoir
 */
public interface UserSpecialtyDetailService {

    List<UserSpecialtyDetail> findByUser_Email(String email);

    List<Tag> findTagByUser_Email(String email);

    void save(UserSpecialtyDetail userSpecialtyDetail);
}
