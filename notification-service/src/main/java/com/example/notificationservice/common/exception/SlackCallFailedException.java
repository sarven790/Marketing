package com.example.notificationservice.common.exception;

public class SlackCallFailedException extends RuntimeException {
    public SlackCallFailedException(String message) { super(message); }
    public SlackCallFailedException(String message, Throwable cause) { super(message, cause); }
}
