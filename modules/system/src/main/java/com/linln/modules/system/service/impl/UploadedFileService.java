package com.linln.modules.system.service.impl;

import org.springframework.stereotype.Service;

@Service
public class UploadedFileService {

    private String uploadedFileName;

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }
}