package com.example.molla.domain.chatroom.exception;

import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;

public class WebSocketException extends BusinessException {

    public WebSocketException(ErrorCode errorCode) {
        super(errorCode);
    }

    public WebSocketException(ErrorCode errorCode, Throwable cause) {
        super(errorCode);
        initCause(cause);
    }
}
