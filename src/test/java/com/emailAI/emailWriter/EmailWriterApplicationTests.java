package com.emailAI.emailWriter;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emailAI.emailWriter.controller.EmailWriterController;
import com.emailAI.emailWriter.entities.EmailRequest;
import com.emailAI.emailWriter.service.EmailWriterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;



@WebMvcTest(EmailWriterController.class)
class EmailWriterApplicationTests {
	@Autowired
    private MockMvc mockMvc; //  HTTP requests

    @MockitoBean
    private EmailWriterService emailWriterService; //service layer

    @InjectMocks
    private EmailWriterController emailController; // Injects the mock into the controller

    private final ObjectMapper objectMapper = new ObjectMapper(); // Converts objects to JSON

	@Test
	void contextLoads() {
	}
	

    @Test
    void testEmailGenerator() throws Exception {
        // Testing EmailRequest

		EmailRequest request = new EmailRequest();
        request.setEmailContent("Can we set up an appointment next week?");
        request.setTone("Professional");

        String requestJson = objectMapper.writeValueAsString(request);

        // Mock  response
        when(emailWriterService.generateEmailReply(request)).thenReturn("Dear Customer, we are happy to assist you");

        // Perform POST 
        mockMvc.perform(post("/api/email/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk()) // Expecting HTTP 200 OK
                .andExpect(content().string("Dear Customer, we are happy to assist you")); //  correct response
   }


}
