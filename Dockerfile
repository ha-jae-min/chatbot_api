FROM openjdk:17-jdk-alpine

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 빌드 인자 (build arguments)
ARG CHATBOT_APIURL
ARG CHATBOT_SECRETKEY
ARG STT_APIURL
ARG STT_SECRETKEY
ARG TTS_APIURL
ARG TTS_APIKEYID
ARG TTS_SECRETKEY

# 환경 변수 설정
ENV CHATBOT_APIURL=$CHATBOT_APIURL \
    CHATBOT_SECRETKEY=$CHATBOT_SECRETKEY \
    STT_APIURL=$STT_APIURL \
    STT_SECRETKEY=$STT_SECRETKEY \
    TTS_APIURL=$TTS_APIURL \
    TTS_APIKEYID=$TTS_APIKEYID \
    TTS_SECRETKEY=$TTS_SECRETKEY

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
