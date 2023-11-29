package com.sparta.myselectshop.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class RestApiException {

    private String errorMessage;
    private int statusCode;
}