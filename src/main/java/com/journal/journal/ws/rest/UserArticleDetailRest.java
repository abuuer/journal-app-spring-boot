/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.service.facade.UserArticleDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @GetMapping("/id/{id}")
   // @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_AUTHOR')")
    public List<UserArticleDetail> findByAuthor_Id(@PathVariable Long id) {
        return userArticleDetailService.findByAuthor_Id(id);
    }
}
