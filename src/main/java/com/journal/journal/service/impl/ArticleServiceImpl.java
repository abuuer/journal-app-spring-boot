/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.FileInfo;
import com.journal.journal.bean.User;
import com.journal.journal.dao.ArticleRepository;
import com.journal.journal.service.facade.ArticleService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.journal.journal.service.facade.FileInfoService;

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

    @Override
    public int save(Article article) {
        Article farticle = articleRepository.findByDoi(article.getDoi());
        int x = 0 ;
        if (farticle != null) {
            x= -1;
        } else {
            for (FileInfo fileInfo : article.getFileInfos()) {
                FileInfo foundedFile = fileService.findById(fileInfo.getId());
                if (foundedFile == null) {
                    x= -2;
                } else {
                    articleRepository.save(article);
                    foundedFile.setArticle(article);
                    fileService.save(foundedFile);
                    x= 1;
                }
            }
        }
        return x;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

}
