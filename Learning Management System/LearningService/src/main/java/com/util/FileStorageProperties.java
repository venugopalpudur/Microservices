package com.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ConfigurationProperties(prefix = "file")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class FileStorageProperties {
    private String uploadDir;

}