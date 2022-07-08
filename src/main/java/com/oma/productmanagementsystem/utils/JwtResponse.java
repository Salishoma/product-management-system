package com.oma.productmanagementsystem.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class JwtResponse<T> implements Serializable {
    private final T body;
    private final String jwtToken;
}