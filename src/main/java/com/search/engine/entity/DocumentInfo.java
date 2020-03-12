package com.search.engine.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentInfo {
    private double termFrequency;
    private int wordFrequencyInDocument = 1;
}
