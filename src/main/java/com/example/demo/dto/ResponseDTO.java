package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseDTO<T> {
    private String error;
    private List<T> data;
}
