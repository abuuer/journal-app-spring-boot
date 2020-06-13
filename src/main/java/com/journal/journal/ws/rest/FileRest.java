/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.FileInfo;
import com.journal.journal.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.journal.journal.service.facade.FileInfoService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author anoir
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/file")
public class FileRest {

    @Autowired
    private FileInfoService fileService;

    @PostMapping("/upload/type/{fileType}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,@PathVariable String fileType) {
        return fileService.uploadFile(file, fileType);
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        System.out.println("/files");
        return fileService.getListFiles();
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return fileService.getFile(filename);
    }

    @PostMapping("/save")
    public void save(@RequestBody FileInfo file) {
        fileService.save(file);
    }
    
    

}
