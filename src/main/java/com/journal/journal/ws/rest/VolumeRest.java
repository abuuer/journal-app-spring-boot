/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest;

import com.journal.journal.bean.Volume;
import com.journal.journal.service.facade.VolumeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author anoir
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("journal-api/volume")
public class VolumeRest {

    @Autowired
    private VolumeService volumeService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Volume volume) {
        return volumeService.save(volume);
    }

    @GetMapping("/findByNumber/number/{number}")
    public Volume findByNumber(@PathVariable int number) {
        return volumeService.findByNumber(number);
    }

    @GetMapping("/findAll")
    public List<Volume> findAll() {
        return volumeService.findAll();
    }

    @GetMapping("/findAllPublished")
    public List<Volume> findAllPublished() {
        return volumeService.findAllPublished();
    }

}
