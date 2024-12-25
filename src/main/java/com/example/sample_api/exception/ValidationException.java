package com.example.sample_api.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errorMessage;

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(List<String> errorMessage) {
        super("バリデーションエラーが発生しました");
        this.errorMessage = errorMessage;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }
}
