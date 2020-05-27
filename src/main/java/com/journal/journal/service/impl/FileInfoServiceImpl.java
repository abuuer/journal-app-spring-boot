/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.service.impl;

import com.journal.journal.bean.FileInfo;
import com.journal.journal.dao.FileInfoRepository;
import com.journal.journal.message.ResponseMessage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.util.FileSystemUtils;
import com.journal.journal.service.facade.FileInfoService;
import com.journal.journal.ws.rest.FileRest;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anoir
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void storeFile(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public ResponseEntity<ResponseMessage> uploadFiles(MultipartFile[] files) {
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(files).stream().forEach(file -> {
                storeFile(file);
                fileNames.add(file.getOriginalFilename());
                Resource currentfile = loadFile(file.getOriginalFilename());
                //MvcUriComponentsBuilder used tp generate url
                String url = MvcUriComponentsBuilder.fromMethodName(FileRest.class, "getFile", currentfile.getFilename()).build().toString();
                FileInfo newFile = new FileInfo(currentfile.getFilename(), url);
                saveInfo(newFile);
            });
            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseMessage(message));
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileRest.class, "getFile", path.getFileName().toString()).build().toString();
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @Override
    public ResponseEntity<Resource> getFile(String filename) {
        Resource file = loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + file.getFilename() + "\"").body(file);
    }

    @Override
    public int saveInfo(FileInfo file) {
        Optional<FileInfo> foundedFile = fileInfoRepository.findById(file.getId());
        if(foundedFile.isPresent()){
            return -1;
        } else {
            fileInfoRepository.save(file);
            return 1;
        }
    }

    @Override
    public FileInfo findById(Long Id) {
        return fileInfoRepository.findById(Id).get();
    }

    @Override
    public void save(FileInfo file) {
        fileInfoRepository.save(file);
    }

    @Override
    public FileInfo findByReference(String reference) {
        return fileInfoRepository.findByReference(reference);
    }
}