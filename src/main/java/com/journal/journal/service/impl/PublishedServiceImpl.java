/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.Article;
import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Published;
import com.journal.journal.dao.PublishedRepositoy;
import com.journal.journal.security.payload.response.MessageResponse;
import com.journal.journal.service.facade.ArticleService;
import com.journal.journal.service.facade.IssueService;
import com.journal.journal.service.facade.PublishedService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author anoir
 */
@Service
public class PublishedServiceImpl implements PublishedService {

    @Autowired
    private PublishedRepositoy publishedRepositoy;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private IssueService issueService;

    @Override
    public ResponseEntity<?> deleteArticleFromIssue(String articleRef) {
        publishedRepositoy.delete(publishedRepositoy.findByArticle_Reference(articleRef));
        return ResponseEntity.ok(new MessageResponse("Deleted article from issue successfully"));
    }

    @Override
    public List<Article> findByIssue_Number(int issueNumber) {
        List<Published> fPublished = publishedRepositoy.findByIssue_Number(issueNumber);
        List<Article> listArticle = new ArrayList<>();
        fPublished.forEach((published) -> {
            listArticle.add(published.getArticle());
        });
        return listArticle;
    }

    @Override
    public ResponseEntity<?> addToIssue(String articleRef, int issueNumber, int volNumber) {
        Issue fIssue = issueService.findByNumberAndVolume_Number(issueNumber, volNumber);
        Article fArticle = articleService.findByReference(articleRef);

        if (fArticle == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Article doesn't exist"));
        } else {
            if (fIssue == null) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("This issue number doesn't exist yet. please create the issue first!"));
            } else {
                Published p = new Published(fArticle, fIssue);
                return ResponseEntity.ok(new MessageResponse("Article has been added to issue number "
                        + fIssue.getNumber() + " successfully"));
            }
        }
    }

    @Override
    public void addClick(String reference) {
        Published fArticle = publishedRepositoy.findByArticle_Reference(reference);
        fArticle.setClicks(fArticle.getClicks() + 1);
        publishedRepositoy.save(fArticle);
    }

    @Override
    public List<Article> findMostRead() {
        List<Published> fPublished = publishedRepositoy.findMostRead();
        List<Article> listArticle = new ArrayList<>();
        fPublished.forEach((published) -> {
            listArticle.add(published.getArticle());
        });
        return listArticle;
    }

}
