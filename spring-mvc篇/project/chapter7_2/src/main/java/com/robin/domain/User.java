package com.robin.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private MultipartFile image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
