package org.sunbong.chatbot_api.voice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class VoiceService {

    private final RestTemplate restTemplate;

    @Value("${STT_APIURL}")
    private String sttApiUrl;
    @Value("${STT_SECRETKEY}")
    private String sttSecretKey;

    @Value("${TTS_APIURL}")
    private String ttsApiUrl;
    @Value("${TTS_APIKEYID}")
    private String ttsApiKeyId;
    @Value("${TTS_SECRETKEY}")
    private String ttsSecretKey;

    public String convertSTT(MultipartFile voiceFile) throws IOException {

        log.info("==============================");
        log.info("VoiceService");
        log.info("==============================");

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/octet-stream");
        headers.set("X-CLOVASPEECH-API-KEY", sttSecretKey);

        // 요청 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(sttApiUrl)
                .queryParam("lang", "Kor")
                .queryParam("assessment", "false")
                .queryParam("utterance", "")
                .queryParam("graph", "false");

        // HTTP 요청 생성
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(voiceFile.getBytes(), headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // 응답에서 텍스트 추출
        Map<String, Object> responseBody = response.getBody();
        return (String) responseBody.getOrDefault("text", "인식된 텍스트가 없습니다.");
    }

    public byte[] convertTTS(String text) {
        log.info("Text to Speech Conversion");


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", ttsApiKeyId);
        headers.set("X-NCP-APIGW-API-KEY", ttsSecretKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        log.info("TTS 요청 텍스트: " + text);

        // Map 쓰면 오류나서 대신 LinkedMultiValueMap 사용
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("speaker", "nara");
        body.add("text", text);
        body.add("volume", "0");
        body.add("speed", "0");
        body.add("pitch", "0");
        body.add("format", "wav");

        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(ttsApiUrl, HttpMethod.POST, requestEntity, byte[].class);
            log.info("TTS 응답 상태 코드: " + response.getStatusCode());
            log.info("TTS 응답 헤더: " + response.getHeaders());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                log.info("여기까지 성공=======================");

                return response.getBody();
            } else {
                log.error("TTS 변환 실패, 상태 코드: " + response.getStatusCode());
                throw new RuntimeException("음성 변환에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("TTS 요청 중 오류 발생: ", e);
            throw new RuntimeException("TTS 요청 중 오류 발생: " + e.getMessage());
        }
    }





}
