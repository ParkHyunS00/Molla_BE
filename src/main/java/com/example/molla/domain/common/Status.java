package com.example.molla.domain.common;

public enum Status {
    DONE, // ML 응답 완료된 상태
    ERROR, // 에러 발생한 상태
    ANALYSIS, // 사용자가 전송한 메시지를 ML에 전달할 때
    CHAT // 사용자가 전송한 메시지를 ML에 전달할 때
}