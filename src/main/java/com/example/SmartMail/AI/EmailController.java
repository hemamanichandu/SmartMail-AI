package com.example.SmartMail.AI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController
{
    @Autowired
    private EmailService emailService;

    @PostMapping("/reply")
    public ResponseEntity<?> reply(@RequestParam String email,@RequestParam String theirText, @RequestParam String subject) throws Exception {

        if(email.contains("gmail"))

        {String response= emailService.generateReply(email,theirText,subject);

        return ResponseEntity.ok(response);
        }
        String req="failed";
        return ResponseEntity.badRequest().body(req);
    }


    @PostMapping("/bomber")
    public int boomber(@RequestParam String email,@RequestParam String theirText, @RequestParam String subject)
    throws Exception{
       int count= emailService.called(email,theirText,subject);

        return count;
    }


    @PostMapping("/resumeSender")
    public ResponseEntity<String> apply(@RequestParam String email,@RequestParam String jd, @RequestParam String role )
            throws Exception
    {
        if(email.contains("@gmail.com")) {
            return emailService.sentResume(email, jd, role);
        }
        return ResponseEntity.ok("failed");
    }










}