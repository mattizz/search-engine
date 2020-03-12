package com.search.engine.service;

import com.search.engine.config.StorageProperties;
import com.search.engine.exception.BadFileException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static com.search.engine.exception.BadFileException.*;
import static java.util.Objects.requireNonNull;

@Service
public class StorageService {

    public static final String DUPLICATE_FILENAME_ERROR = "There is a file with this name in storage: ";
    private final Path ROOT;

    public StorageService(StorageProperties storageProperties) {
        this.ROOT = Paths.get(storageProperties.getLocation());
    }

    @PostConstruct
    public void createUploadDirectory() throws IOException {
        Files.createDirectories(ROOT);
    }

    @PreDestroy
    public void deleteUploadDirectory() {
        FileSystemUtils.deleteRecursively(ROOT.toFile());
    }

    public void store(MultipartFile file) {
        requireNonNull(file, FILE_NULL_ERROR);

        valid(file);
        saveFile(file);
    }

    private void saveFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, ROOT.resolve(requireNonNull(file.getOriginalFilename())),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadFileException(SAVE_FILE_ERROR);
        }
    }

    private void valid(MultipartFile inputFile) {
        if (inputFile.isEmpty()) {
            throw new BadFileException(EMPTY_FILE_ERROR);
        }

        if (inputFile.getOriginalFilename().contains("..")) {
            throw new BadFileException(FILENAME_ERROR + inputFile.getOriginalFilename());
        }

        File[] files = ROOT.toFile().listFiles();
        if (files != null) {
            boolean isExistFileWithTheSameName = Arrays.stream(files)
                    .anyMatch(existingFile -> inputFile.getOriginalFilename().equals(existingFile.getName()));

            if (isExistFileWithTheSameName) {
                throw new BadFileException(DUPLICATE_FILENAME_ERROR + inputFile.getOriginalFilename());
            }
        }
    }
}
