package com.search.engine.controller;

import com.search.engine.dto.DocumentResponse;
import com.search.engine.service.DocumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class UploadController {

    private final DocumentService documentService;

    public UploadController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/single")
    public DocumentResponse uploadFile(@RequestParam("file") MultipartFile file) {

        documentService.processFile(file);
        return new DocumentResponse(file.getOriginalFilename(), file.getSize());
    }

    @PostMapping("/multiple")
    public List<DocumentResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }
}
