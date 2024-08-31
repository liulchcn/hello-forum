package com.hello.service.studio.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

public class CustomMultipartFile extends MockMultipartFile {

    public CustomMultipartFile(byte[] content, String fileName, String contentType) {
        super(fileName, fileName, contentType, content);
    }
}
