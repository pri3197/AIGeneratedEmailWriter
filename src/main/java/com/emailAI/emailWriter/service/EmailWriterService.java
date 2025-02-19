package com.emailAI.emailWriter.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.emailAI.emailWriter.entities.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailWriterService {
    
    
    private final WebClient webClient;
    @Autowired
    public EmailWriterService(WebClient.Builder webClient){
        this.webClient=webClient.build();
    }

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
 public String generateEmailReply(EmailRequest emailRequest){
  
    //build a prompt 
    String prompt=buildPrompt(emailRequest);
    //make a request
    Map<String,Object> request=Map.of("contents",new Object[]{
                                Map.of("parts", new Object[]{
                                    Map.of("text", prompt)
                                })
    });  

    //Send request and receive response
    String response=webClient.post().uri(geminiApiUrl+geminiApiKey)
    .header("Content-Type", "application/json")
    .bodyValue(request) // Use bodyValue instead of body()
    .retrieve() // This is required before bodyToMono()
    .bodyToMono(String.class)
    .block();

    return extractResponseContent(response);
 }

 private String extractResponseContent(String response){
    try{
        ObjectMapper mapper= new ObjectMapper();
        JsonNode rootNode=mapper.readTree(response);
        return rootNode.path("candidates")
    .get(0)
    .path("content")
    .path("parts")
    .get(0)
    .path("text")
    .asText();
}
    catch(Exception e){
        return "Error processing request: "+e.getMessage();
    }
 }

 public String buildPrompt(EmailRequest emailRequest){
    StringBuilder prompt=new StringBuilder();
    prompt.append("Generate a professional email reply for the following email content. Please do not generate a subject line.");
    if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
        prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");

    }
    prompt.append("\nOriginal email: \n:").append(emailRequest.getEmailContent());
    return prompt.toString();
 }
}
