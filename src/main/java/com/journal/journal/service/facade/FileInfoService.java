/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.facade;

import com.journal.journal.bean.FileInfo;
import com.journal.journal.message.ResponseMessage;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anoir
 */
public interface FileInfoService {
    
 

    public void save(FileInfo file);

    public void init();

    public FileInfo findById(Long Id);

    public int saveInfo(FileInfo file);

    public void storeFile(MultipartFile file);

    public Resource loadFile(String filename);

    public Stream<Path> loadAll();

    public void deleteAll();

    public ResponseEntity<ResponseMessage> uploadFiles(MultipartFile[] files);

    public ResponseEntity<List<FileInfo>> getListFiles();

    public ResponseEntity<Resource> getFile(String filename);

}
