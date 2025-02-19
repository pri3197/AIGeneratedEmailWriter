package com.emailAI.emailWriter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailAI.emailWriter.entities.EmailRequest;
import com.emailAI.emailWriter.service.EmailWriterService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins= "*")
public class EmailWriterController {
    private final EmailWriterService emailWriterService;
    @PostMapping("/generate")    
    public ResponseEntity<String> emailGenerator(@RequestBody EmailRequest emailRequest){
        String response = emailWriterService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
}
