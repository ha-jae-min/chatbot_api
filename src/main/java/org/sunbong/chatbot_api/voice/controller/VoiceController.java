package org.sunbong.chatbot_api.voice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.chatbot_api.voice.service.VoiceService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/napi/v1/voice")
@Log4j2
@RequiredArgsConstructor
public class VoiceController {

    private final VoiceService voiceService;

    @PostMapping("stt")
    public ResponseEntity<String> convertSTT(@RequestParam("voiceFile") MultipartFile voiceFile) throws IOException {

        log.info("===================================");
        log.info("VoiceController");

        String recognizedText = voiceService.convertSTT(voiceFile);

        log.info("Recognized text: " + recognizedText);

        return ResponseEntity.ok(recognizedText);
    }

    @PostMapping("tts")
    public ResponseEntity<byte[]> convertTTS(@RequestBody Map<String, String> request) {

        String text = request.get("text");
        byte[] audioData = voiceService.convertTTS(text);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"output.mp3\"")
                .body(audioData);
    }



}
