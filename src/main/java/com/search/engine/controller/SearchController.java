package com.search.engine.controller;

import com.search.engine.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class SearchController {

    private final DocumentService documentService;

    public SearchController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/keywords")
    public ResponseEntity<Set<String>> getDocumentsContaining(@RequestParam("keyword") String keyword) {
        return ok(documentService.findDocumentsContaining(keyword));
    }

    @GetMapping("/infos")
    public Map<String, Double> getTFIDFValueFor(@RequestParam("keyword") String keyword) {
        return documentService.getTFIDF(keyword);
    }
}
