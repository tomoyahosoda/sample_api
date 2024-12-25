package com.example.sample_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String bookId) {
        super("Bookが見つかりませんでした。 IDは" + bookId + "です。");
    }
}
