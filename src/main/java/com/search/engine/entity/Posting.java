package com.search.engine.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Posting {
    private Map<String, DocumentInfo> documents = new HashMap<>();
}
