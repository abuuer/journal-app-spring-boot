/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.ArticleTagsDetail;
import com.journal.journal.bean.FileInfo;
import com.journal.journal.bean.Tag;
import com.journal.journal.bean.User;
import com.journal.journal.bean.UserArticleDetail;
import com.journal.journal.bean.UserSpecialtyDetail;
import com.journal.journal.dao.ArticleRepository;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.ArticleTagsDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.service.facade.TagService;
import com.journal.journal.service.facade.UserArticleDetailService;
import com.journal.journal.service.facade.UserService;
import com.journal.journal.service.facade.UserSpecialtyDetailService;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author anoir
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private FileInfoService fileService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagsDetailService articleTagsDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserArticleDetailService userArticleDetailService;
    @Autowired
    private UserSpecialtyDetailService userSpecialtyDetailService;

    @Override
    public int save(Article article) {
        Article farticle = articleRepository.findByReference(article.getReference());
        List<Tag> tags = new ArrayList<>();
        if (farticle != null) {
            return -1;
        } else {
            articleRepository.save(article);
            for (FileInfo fileInfo : article.getFileInfos()) {
                FileInfo foundedFile = fileService.findByReference(fileInfo.getReference());
                if (foundedFile == null) {
                    System.out.println("foundedFile == null");
                } else {
                    foundedFile.setArticle(article);
                    fileService.save(foundedFile);
                }
            }
            for (ArticleTagsDetail articleTagsDetail : article.getArticleTags()) {
                Tag tag = tagService.findByName(articleTagsDetail.getTag().getName());
                tags.add(tag);
                if (tag == null) {
                    tagService.save(articleTagsDetail.getTag());
                    ArticleTagsDetail newAT = new ArticleTagsDetail(article, articleTagsDetail.getTag());
                    articleTagsDetailService.save(newAT);
                } else {
                    ArticleTagsDetail newAT = new ArticleTagsDetail(article, tag);
                    articleTagsDetailService.save(newAT);
                }
            }
            if (article.getUserArticleDetails() != null) {
                for (UserArticleDetail userArticleDetail : article.getUserArticleDetails()) {
                    Optional<User> fUser = userService.findByEmail(userArticleDetail.getUser().getEmail());
                    // save user speacialty from article submission
					if(fUser.isPresent()){
						UserArticleDetail uad = new UserArticleDetail("Author", fUser.get(), article);
                        userArticleDetailService.save(uad);
						List<Tag> s = userSpecialtyDetailService.findTagByUser_Email(fUser.get().getEmail());
                    for (Tag tag : tags) {
                        if (!s.contains(tag)) {
                            UserSpecialtyDetail usd = new UserSpecialtyDetail(fUser.get(), tag);
                            userSpecialtyDetailService.save(usd);
                        }
                    }
					}else {
						userService.save(userArticleDetail.getUser());
						for (Tag tag : tags) {
                            UserSpecialtyDetail usd = new UserSpecialtyDetail(userArticleDetail.getUser(), tag);
                            userSpecialtyDetailService.save(usd);
                         }
                        UserArticleDetail uad = new UserArticleDetail("Author", userArticleDetail.getUser(), article);
                        userArticleDetailService.save(uad);
						userService.save(userArticleDetail.getUser());
					}
                }
            } else {
                return -3;
            }
            return 1;
        }
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public ResponseEntity<?> assignReviewer(String articleRef, String email) {
        Article fArticle = articleRepository.findByReference(articleRef);
        List<UserArticleDetail> userArticles = userArticleDetailService.findByArticle_Reference(articleRef);
        List<User> freviewers = new ArrayList<>();
        for (UserArticleDetail userArticle : userArticles) {
            freviewers.add(userArticle.getUser());
        }
        Optional<User> freviewer = userService.findByEmail(email);
        if (!freviewer.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Reviewer doesn't exist"));
        } else if (freviewers.contains(freviewer.get())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(freviewer.get().getLastName()
                            + " is already assigned as a reviewer to this article"));
        } else if (fArticle == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Article doesn't exist"));
        } else {
            UserArticleDetail uad = new UserArticleDetail("Reviewer", freviewer.get(), fArticle);
            userArticleDetailService.save(uad);
            return ResponseEntity.ok(new MessageResponse("Assigned " + freviewer.get().getFirstName() + " "
                    + freviewer.get().getLastName() + " to the article"));
        }
    }
	
	@Override
    public ResponseEntity<?> dismissReviewer(String articleRef, Long id) {
        return ResponseEntity.ok(new MessageResponse("Assigned "));
    }

    @Override
    public Article findByReference(String referenece) {
        return articleRepository.findByReference(referenece);
    }

}
