/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.facade;

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
    public ResponseEntity<?> assignReviewer(@PathVariable String articleRef, @PathVariable String email) {
        return articleService.assignReviewer(articleRef, email);
    }

    @PutMapping("/dismissReviewer/articleRef/{articleRef}/email/{email}")
    public ResponseEntity<?> dismissReviewer(String articleRef, String email) {
        return articleService.dismissReviewer(articleRef, email);
    }

    @PutMapping("/updateStatus/articleRef/{articleRef}/status/{status}/decision/{decision}")
    public ResponseEntity<?> updateStatus(@PathVariable String articleRef
            , @PathVariable String status, @PathVariable String decision) {
        return articleService.updateStatus(articleRef, status, decision);
    }

    @GetMapping("/findByStatus/status/{status}")
    public List<Article> findByStatus(@PathVariable String status) {
        return articleService.findByStatus(status);
    }

}
