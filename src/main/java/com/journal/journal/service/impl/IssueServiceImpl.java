/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.FileInfo;
import com.journal.journal.bean.Issue;
import com.journal.journal.bean.Volume;
import com.journal.journal.dao.IssueRepository;
import com.journal.journal.service.util.message.ResponseMessage;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.service.facade.IssueService;
import com.journal.journal.service.facade.VolumeService;
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
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private VolumeService volumeService;

    @Autowired
    private FileInfoService fileService;

    @Override
    public ResponseEntity<?> save(Issue issue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<?> createNewIssue(Issue issue) {
        Volume fVolume = volumeService.findByNumber(issue.getVolume().getNumber());
        if (fVolume == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("This volume has not been created yet"));
        } else {
            List<Issue> fIssues = issueRepository.findByVolume_Number(fVolume.getNumber());
            List<Integer> numbers = new ArrayList<>();
            for (Issue fIssue : fIssues) {
                numbers.add(fIssue.getNumber());
            }
            if (numbers.contains(issue.getNumber())) {
                return ResponseEntity.badRequest().body(new ResponseMessage("This issue has been already created"));
            } else {
                Issue newIssue = new Issue(issue.getNumber() ,"On hold",
                       issue.getStartMonth(),  issue.getEndMonth(), issue.getIssn(), fVolume);
                issueRepository.save(newIssue);
                /*for (FileInfo fileInfo : issue.getFileInfos()) { 
                    FileInfo foundedFile = fileService.findByReference(fileInfo.getReference());
                    if (foundedFile == null) {
                        System.out.println("foundedFile == null");
                    } else {
                        foundedFile.setIssue(issue);
                        fileService.save(foundedFile);
                    }
                }*/
                return ResponseEntity.ok(new ResponseMessage("Issue created and added to volume number "
                        + fVolume.getNumber()));
            }
        }
    }

    @Override
    public List<Issue> findByVolume_Number(int volumeNumber) {
        return issueRepository.findByVolume_Number(volumeNumber);
    }

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    @Override
    public Issue findByNumberAndVolume_Number(int issNumber, int volNumber) {
         return issueRepository.findByNumberAndVolume_Number(issNumber, volNumber);
    }

    @Override 
    public List<Issue> findAllPublished() {
        return issueRepository.findAllPublished();
    }

    @Override
    public ResponseEntity<?> publishIssue(int issNumber, int volNumber) {
        Issue fIssue = issueRepository.findByNumberAndVolume_Number(issNumber, volNumber);
        if(fIssue == null){
            return ResponseEntity.badRequest().body(new ResponseMessage("Issue doesn't exist"));
        }else {
            fIssue.setPublishDate(new Date());
            fIssue.setStatus("published");
            issueRepository.save(fIssue);
            return ResponseEntity.ok(new ResponseMessage("Issue published successfully"));
        }
    }

    @Override
    public List<Issue> findAllPublishedByVol(int volNum) {
        return issueRepository.findAllPublishedByVol(volNum);
    }

    @Override
    public Issue findLatestIssue() {
        return issueRepository.findLatestIssue();
    }
}
