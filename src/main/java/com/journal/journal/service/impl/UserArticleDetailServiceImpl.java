/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.dao.UserArticleDetailRepository;
import com.journal.journal.service.facade.UserArticleDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserArticleDetailServiceImpl implements UserArticleDetailService{

    @Autowired
    private UserArticleDetailRepository repository;
    
    @Override
    public void save(UserArticleDetail userArticleDetail) {
        repository.save(userArticleDetail);
    }

    @Override
    public List<UserArticleDetail> findByAuthor_Id(Long id) {
        return repository.findByAuthor_Id(id);
    }

    
}
