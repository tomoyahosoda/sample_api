package com.example.sample_api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiError {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("documentation_url")
    private String documentationUrl;
}
