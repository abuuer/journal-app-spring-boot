/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.service.facade.UserArticleDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/user-article")
public class UserArticleDetailRest {
    
    
    @Autowired
    private UserArticleDetailService userArticleDetailService;

    @GetMapping("/findAllArticlesByReviewer/email/{email}")
    public List<Article> findAllArticlesByReviewer(@PathVariable String email) {
        return userArticleDetailService.findAllArticlesByReviewer(email);
    }

    @GetMapping("/findAllArticlesByAuthor/email/{email}")
    public List<Article> findAllArticlesByAuthor(@PathVariable String email) {
        return userArticleDetailService.findAllArticlesByAuthor(email);
    }

    @DeleteMapping("/deleteByUserId/email/{email}/articleRef/{reference}")
    public ResponseEntity<?> deleteByUser_EmailAndArticle_Reference(@PathVariable String email,@PathVariable String reference) {
        return userArticleDetailService.eleteByUser_EmailAndArticle_Reference(email, reference);
    }

    
}
