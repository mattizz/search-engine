package com.search.engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentResponse {

    private String name;

    private long size;

    public DocumentResponse(String name, long size) {
        this.name = name;
        this.size = size;
    }
}
