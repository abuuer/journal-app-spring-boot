/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Volume;
import com.journal.journal.service.facade.IssueService;
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
@RequestMapping("journal-api/issue")
public class IssueRest {

    @Autowired
    private IssueService issueService;

    @PostMapping("/createNewIssue")
    public ResponseEntity<?> createNewIssue(@RequestBody Issue issue) {
        return issueService.createNewIssue(issue);
    }

    @GetMapping("/findByVolume_Number/volumeNumber/{volumeNumber}")
    public List<Issue> findByVolume_Number(@PathVariable int volumeNumber) {
        return issueService.findByVolume_Number(volumeNumber);
    }

    @GetMapping("/findAll")
    public List<Issue> findAll() {
        return issueService.findAll();
    }

    @GetMapping("/findByNumberAndVolume/issNumber/{issNumber}/volNumber/{volNumber}")
    public Issue findByNumberAndVolume_Number(@PathVariable int issNumber, @PathVariable int volNumber) {
        return issueService.findByNumberAndVolume_Number(issNumber, volNumber);
    }

    @GetMapping("/findAllPublished")
    public List<Issue> findAllPublished() {
        return issueService.findAllPublished();
    }

    @PutMapping("/publishIssue/issNumber/{issNumber}/volNumber/{volNumber}")
    public ResponseEntity<?> publishIssue(@PathVariable int issNumber, @PathVariable int volNumber) {
        return issueService.publishIssue(issNumber, volNumber);
    }

    @GetMapping("/findAllPublishedByVol/volNum/{volNum}")
    public List<Issue> findAllPublishedByVol(@PathVariable int volNum) {
        return issueService.findAllPublishedByVol(volNum);
    }

    @GetMapping("/findLatestIssue")
    public Issue findLatestIssue() {
        return issueService.findLatestIssue();
    }

}
