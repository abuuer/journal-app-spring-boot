/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.Article;
import com.journal.journal.service.facade.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/article")
public class ArticleRest {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/save")
    public int save(@RequestBody Article article) {
        return articleService.save(article);
    }

    @GetMapping("/all")
    public List<Article> findAll() {
        return articleService.findAll();
    }

    @PutMapping("/assignReviewer/articleRef/{articleRef}/email/{email}")
    public ResponseEntity<?> assignReviewer(@PathVariable String articleRef,@PathVariable String email) {
        return articleService.assignReviewer(articleRef,email);
    }
    
}
