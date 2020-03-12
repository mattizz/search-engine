package com.search.engine.service;

import com.search.engine.exception.BadFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.search.engine.exception.BadFileException.FILE_NULL_ERROR;
import static com.search.engine.exception.BadFileException.READ_FILE_ERROR;
import static java.util.Objects.requireNonNull;

@Service
public class DocumentService {

    public static final String KEYWORD_NULL_ERROR = "Keyword can't be null";
    public static final String EMPTY_KEYWORD_ERROR = "Keyword is empty";
    public static final String NEW_LINE = "\n";

    private final StorageService storageService;
    private final SearchEngineService searchEngineService;

    public DocumentService(StorageService storageService, SearchEngineService searchEngineService) {
        this.storageService = storageService;
        this.searchEngineService = searchEngineService;
    }

    public Set<String> findDocumentsContaining(String keyword) {
        valid(keyword);

        return searchEngineService.getDocumentsContaining(keyword);
    }

    private void valid(String keyword) {
        requireNonNull(keyword, KEYWORD_NULL_ERROR);

        if (keyword.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_KEYWORD_ERROR);
        }
    }

    public void processFile(MultipartFile file) {
        requireNonNull(file, FILE_NULL_ERROR);

        storageService.store(file);
        String text = getFileContent(file);
        searchEngineService.createInvertedIndexStructure(text, file.getOriginalFilename());
    }

    private String getFileContent(MultipartFile file) {
        String content = "";

        try (InputStream inputStream = file.getInputStream()) {
            content = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining(NEW_LINE));
        } catch (IOException e) {
            throw new BadFileException(READ_FILE_ERROR);
        }

        return content;
    }

    public Map<String, Double> getTFIDF(String keyword) {
        requireNonNull(keyword, KEYWORD_NULL_ERROR);

        return searchEngineService.calculateTFIDFValuesFor(keyword);
    }
}
