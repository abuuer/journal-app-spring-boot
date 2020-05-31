/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import java.util.List;

/**
 *
 * @author anoir
 */
public interface UserArticleDetailService {

    void save(UserArticleDetail userArticleDetail);

   // List<UserArticleDetail> findByAuthor_Id(Long id);
    
    List<UserArticleDetail> findByArticle_Reference(String reference);
}
