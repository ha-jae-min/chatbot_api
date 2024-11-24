package org.sunbong.chatbot_api.chatbot.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Log4j2
@Service
public class ChatbotService {

    @Value("${CHATBOT_SECRETKEY}")
    private String secretKey;

    public String generateChatbotSignature(String requestBody) throws NoSuchAlgorithmException, InvalidKeyException {

        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

        Mac mac = Mac.getInstance("HmacSHA256");

        mac.init(secretKeySpec);

        byte[] signature = mac.doFinal(requestBody.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(signature);
    }


}
