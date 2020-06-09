/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.dao;

import com.journal.journal.bean.UserArticleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author anoir
 */
@Repository
public interface UserArticleDetailRepository extends JpaRepository<UserArticleDetail, Long> {

    List<UserArticleDetail> findByUser_Email(String email);

    List<UserArticleDetail> findByArticle_Reference(String reference);
    
    @Transactional
    void deleteByUser_EmailAndArticle_Reference(String email, String reference);
    
}
