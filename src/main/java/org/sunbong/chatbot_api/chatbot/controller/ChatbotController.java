package org.sunbong.chatbot_api.chatbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.sunbong.chatbot_api.chatbot.service.ChatbotService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/chatbot")
@Log4j2
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Value("${CHATBOT_APIURL}")
    private String apiUrl;

    @PostMapping()
    public ResponseEntity<String> sendChatbotRequest(@RequestBody String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {
        // 챗봇 서명 생성
        String signature = chatbotService.generateChatbotSignature(requestBody);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CHATBOT_SIGNATURE", signature);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 엔티티 설정
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();

        // 챗봇 API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        // 응답 바디 로그
        log.info("응답 바디: " + response.getBody());

        return response;
    }

}
