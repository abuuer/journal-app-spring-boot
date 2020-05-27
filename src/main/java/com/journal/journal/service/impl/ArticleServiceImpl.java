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
import com.journal.journal.dao.ArticleRepository;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.ArticleTagsDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.service.facade.TagService;
import com.journal.journal.service.facade.UserArticleDetailService;
import com.journal.journal.service.facade.UserService;
import java.util.Optional;

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

    @Override
    public int save(Article article) {
        Article farticle = articleRepository.findByDoi(article.getDoi());

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
                    Optional<User> fUser = userService.findByEmail(userArticleDetail.getAuthor().getEmail());
                    if (!fUser.isPresent()) {
                        System.out.println(userArticleDetail.getAuthor());
                        userService.save(userArticleDetail.getAuthor());
                        UserArticleDetail uad = new UserArticleDetail(userArticleDetail.getAuthor(), article);
                        userArticleDetailService.save(uad);
                    } else {
                        UserArticleDetail uad = new UserArticleDetail(fUser.get(), article);
                        userArticleDetailService.save(uad);
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

}
