/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.dao.UserArticleDetailRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.UserArticleDetailService;
import com.journal.journal.service.facade.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class UserArticleDetailServiceImpl implements UserArticleDetailService {

    @Autowired
    private UserArticleDetailRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Override
    public void save(UserArticleDetail userArticleDetail) {
        repository.save(userArticleDetail);
    }

    @Override
    public List<UserArticleDetail> findByArticle_Reference(String reference) {
        return repository.findByArticle_Reference(reference);
    }

    @Override
    public List<Article> findAllArticlesByReviewer(String email) {
        List<UserArticleDetail> userArticleDetails = repository.findByUser_Email(email);
        List<Article> articles = new ArrayList<>();
        userArticleDetails.stream().filter((userArticleDetail) -> (userArticleDetail.getFunction().equals("Reviewer"))).forEachOrdered((userArticleDetail) -> {
            articles.add(userArticleDetail.getArticle());
        });
        return articles;
    }

    @Override
    public List<Article> findAllArticlesByAuthor(String email) {
        List<UserArticleDetail> userArticleDetails = repository.findByUser_Email(email);
        List<Article> articles = new ArrayList<>();
        userArticleDetails.stream().filter((userArticleDetail) -> (userArticleDetail.getFunction().equals("Author"))).forEachOrdered((userArticleDetail) -> {
            articles.add(userArticleDetail.getArticle());
        });
        return articles;
    }

    @Override
    public ResponseEntity<?> eleteByUser_EmailAndArticle_Reference(String email, String reference) {
        Optional<User> fUser = userService.findByEmail(email);
        Article farticle = articleService.findByReference(reference);
        if (!fUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User doesn't exist"));
        } else if (farticle == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Article doesn't exist"));
        } else {
            repository.deleteByUser_EmailAndArticle_Reference(email, reference);
            return ResponseEntity.ok(new MessageResponse(fUser.get().getFirstName() + " "
                    + fUser.get().getLastName() + " is dismissed"));
        }

    }

    @Override
    public List<UserArticleDetail> findByUser_Email(String email) {
        return repository.findByUser_Email(email);
    }

    @Override
    public void delete(UserArticleDetail userArticleDetail) {
        repository.delete(userArticleDetail);
    }

}
