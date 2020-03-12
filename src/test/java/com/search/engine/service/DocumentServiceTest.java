package com.search.engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.search.engine.exception.BadFileException.FILE_NULL_ERROR;
import static com.search.engine.service.DocumentService.EMPTY_KEYWORD_ERROR;
import static com.search.engine.service.DocumentService.KEYWORD_NULL_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    private DocumentService documentService;
    private SearchEngineService searchEngineService;
    private StorageService storageService;

    @BeforeEach
    void setUp() {
        searchEngineService = mock(SearchEngineService.class);
        storageService = mock(StorageService.class);
        documentService = new DocumentService(storageService, searchEngineService);
    }

    @Test
    public void shouldReturnListWithDocumentsContainingWord() {

        // given
        String goodKeyword = "keyword";
        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Document1");
        when(searchEngineService.getDocumentsContaining(any())).thenReturn(expectedResult);

        // when
        List<String> actualResult = documentService.findDocumentsContaining(goodKeyword);

        // then
        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void shouldCatchIllegalArgumentExceptionWhenKeywordIsEmptyForFindDocumentsContaining() {

        // given
        String emptyKeyword = "";

        // when
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> documentService.findDocumentsContaining(emptyKeyword));

        // then
        assertEquals(actualException.getMessage(), EMPTY_KEYWORD_ERROR);
    }

    @Test
    public void shouldCatchNullPointerExceptionWhenFileNull() {

        // given
        MultipartFile nullFile = null;

        // when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> documentService.processFile(nullFile));

        //then
        assertEquals(actualException.getMessage(), FILE_NULL_ERROR);
    }

    @Test
    void shouldProcessFileAndNotThrowException() {

        assertDoesNotThrow(() -> {

            // given
            MultipartFile file = new MockMultipartFile("name", new byte[20]);
            doNothing().when(storageService).store(any());
            doNothing().when(searchEngineService).createInvertedIndexStructure(anyString(), anyString());

            // when
            documentService.processFile(file);
        });
    }

    @Test
    void shouldCatchNullPointerExceptionWhenKeywordIsNull() {

        //given
        String nullKeyword = null;

        //when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> documentService.getTFIDF(nullKeyword));

        //then
        assertEquals(actualException.getMessage(), KEYWORD_NULL_ERROR);
    }

    @Test
    void shouldCatchIllegalArgumentExceptionWhenKeywordIsEmptyForGetTFIDF() {

        // given
        String emptyKeyword = "";

        // when
        IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class, () -> documentService.getTFIDF(emptyKeyword));

        // then
        assertEquals(actualException.getMessage(), EMPTY_KEYWORD_ERROR);
    }

    @Test
    void shouldReturnMapWhenGetTFIDF() {

        // given
        String goodKeyword = "keyword";
        Map<String, Double> expectedMap = Collections.emptyMap();
        when(searchEngineService.calculateTFIDFValuesFor(anyString())).thenReturn(Collections.emptyMap());

        // when
        Map<String, Double> actualMap = documentService.getTFIDF(goodKeyword);

        // then
        assertEquals(actualMap, expectedMap);
    }
}