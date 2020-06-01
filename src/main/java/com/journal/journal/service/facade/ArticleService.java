/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.User;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
public interface ArticleService {
    
    public int save(Article article);
    
    public List<Article> findAll();
    
    public ResponseEntity<?> assignReviewer(String articleRef,Long id);
	
	public ResponseEntity<?> dismissReviewer(String articleRef,Long id);
    
}
