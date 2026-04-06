package com.example.SmartMail.AI;

import jakarta.mail.internet.MimeMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
public class EmailService {
//    @Autowired
    private ChatClient chatClient;

    @Autowired
    private JavaMailSender mailSender;

        public EmailService(OllamaChatModel model)
        {
            this.chatClient=ChatClient.create(model);
        }
//
//    public String callAi(String prompt)
//    {
//        return chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();
//    }
    int count=0;

    public String generateReply(String email, String theirText, String subject) throws Exception
    {

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(email);

        String subjectPrompt = """
    You are an email subject line assistant.
    Given the incoming email subject: this is the "{subject}" subject
    Generate a suitable, professional reply subject line from my side.
    Note : The subject must be exactly or below the 5 words,
    using clear and appropriate wording for an email subject.
    Ensure it is concise, relevant, and context-aware.
    
    """;
        String replyPrompt = """
    You are an email reply assistant.
    Given the incoming text: this is the "{theirText}" body of the mail
    Generate a suitable, professional reply.
    The reply should:
    - Address the sender politely and professional 
    - Acknowledge their message
    - Provide a clear response or next steps
    - End with a courteous closing and the warm regards and sender name from "HEMA MANI CHANDU DONKA" always
    - note: don't write the subject for mail.
    """;

                PromptTemplate template=new PromptTemplate(subjectPrompt);
                PromptTemplate template1=new PromptTemplate(replyPrompt);
                Prompt prompt=template.create(Map.of("subject",subject));
                Prompt prompt1=template1.create(Map.of("theirText",theirText));

        String subjectReply=chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
        String body=chatClient.prompt(prompt1).call().chatResponse().getResult().getOutput().getText();


        helper.setSubject(subjectReply);
        helper.setText(body);
        mailSender.send(message);
        count=count+1;

        return "sent" ;
    }



    public void bomber(String email, String theirText, String subject) throws Exception
    {

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setTo(email);

        String subjectPrompt = """
    You are an email subject line assistant.
    Given the incoming email subject: this is the "{subject}" subject
    Generate a suitable, professional reply subject line from my side.
    The subject must be exactly or below the 5 words,
    using clear and appropriate wording for an email subject.
    Ensure it is concise, relevant, and context-aware.
    """;
        String replyPrompt = """
    You are an email reply assistant.
    Given the incoming text: this is the "{theirText}" body of the mail
    Generate a suitable, professional reply.
    The reply should:
    - Address the sender politely and professional 
    - Acknowledge their message
    - Provide a clear response or next steps
    - End with a courteous closing and the warm regards and sender name from "HEMA MANI CHANDU DONKA" always
    - note: don't write the subject for mail.
    """;

        PromptTemplate template=new PromptTemplate(subjectPrompt);
        PromptTemplate template1=new PromptTemplate(replyPrompt);
        Prompt prompt=template.create(Map.of("subject",subject));
        Prompt prompt1=template1.create(Map.of("theirText",theirText));

        String subjectReply=chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
        String body=chatClient.prompt(prompt1).call().chatResponse().getResult().getOutput().getText();


        helper.setSubject(subjectReply);
        helper.setText(body);
        mailSender.send(message);
//        count=count+1;

//        return count ;
    }
    public int called(String email, String theirText, String subject) throws Exception
    {
        while (count<10)
        {
            bomber(email,theirText,subject);
            count=count+1;
        }
        return count;
    }


    //resume sender

    public ResponseEntity<String> sentResume(String email, String jd, String role)
    throws Exception
    {

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        try {


        helper.setTo(email);
            String sendingPromptBody = """
Generate ONLY the recruiter email body (do not include a subject line).
Keep the content professional, concise, and under 200 words.
Do not add placeholders or assumptions. Use only the candidate details provided.
Highlight the candidate’s background, technical skills, and enthusiasm for the role.
End with contact information and a polite closing.
Mention that the email was generated using Spring AI.

Candidate Details:
- Name: Hema Mani Chandu Donka
- Graduation: 2025, Sanketika Vidya Parishad Engineering College (affiliated with Andhra University)
- Skills: Java, J2EE, Spring, Spring Boot, Spring AI
- Contact: Mobile – 7396138251
- Email – hemamanichandu@gmail.com

Job Description:
{jd}
""";



     /*   String sendingPromptSubject= """
                Application for  {role} – Resume Attached
                """;*/

        PromptTemplate promptTemplate=new PromptTemplate(sendingPromptBody);
//        PromptTemplate promptTemplate1=new PromptTemplate(sendingPromptSubject);
//        Prompt prompt=promptTemplate1.create(Map.of("role",role));
        Prompt prompt1=promptTemplate.create(Map.of("jd",jd));
//        String subject=chatClient.prompt(prompt).call().content();
            String subjectline=" Application for "+role;
        String body=chatClient.prompt(prompt1).call().content();

        helper.setText(body);
        helper.setSubject(subjectline);
       helper.addAttachment("HEMA-MANI-CHANDU-DONKA_RESUME.pdf",new File("F:\\resumes\\HEMA-MANI-CHANDU-DONKA_RESUME.pdf"));

        mailSender.send(message);
            return ResponseEntity.ok("sent");

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


        return ResponseEntity.ok("failed");

    }



}
