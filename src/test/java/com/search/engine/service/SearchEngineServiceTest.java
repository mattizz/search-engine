package com.search.engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static com.search.engine.service.DocumentService.KEYWORD_NULL_ERROR;
import static com.search.engine.service.SearchEngineService.FILENAME_NULL_MESSAGE;
import static com.search.engine.service.SearchEngineService.TEXT_NULL_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

class SearchEngineServiceTest {

    private SearchEngineService searchEngineService;

    @BeforeEach
    void setUp() {
        searchEngineService = new SearchEngineService();
    }

    @Test
    void shouldCatchNullPointerExceptionWhenTextNull() {

        // given
        String documentName = "exampleName";
        String nullText = null;

        // when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> searchEngineService.createInvertedIndexStructure(nullText, documentName));

        // then
        assertEquals(actualException.getMessage(), TEXT_NULL_MESSAGE);
    }

    @Test
    void shouldCatchNullPointerExceptionWhenDocumentNull() {

        // given
        String documentNameNull = null;
        String text = "text";

        // when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> searchEngineService.createInvertedIndexStructure(text, documentNameNull));

        // then
        assertEquals(actualException.getMessage(), FILENAME_NULL_MESSAGE);
    }

    @Test
    void shouldCreateInvertedIndexStructureAndNotThrowException() {

        assertDoesNotThrow(() -> {
            // given
            String documentName = "Document1";
            String text = "text text, text, text, example text";

            // when
            searchEngineService.createInvertedIndexStructure(documentName, text);
        });
    }

    @Test
    void shouldCatchNullPointerWhenKeywordIsNull() {

        // given
        String keywordNull = null;

        // when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> searchEngineService.getDocumentsContaining(keywordNull));

        // then
        assertEquals(actualException.getMessage(), KEYWORD_NULL_ERROR);
    }

    @Test
    void shouldReturnListWhenCallGetDocumentsContaining() {

        // given
        String keyword = "word";
        List<String> expectedList = Collections.emptyList();

        // when
        List<String> actualList = searchEngineService.getDocumentsContaining(keyword);

        // then
        assertEquals(actualList, expectedList);
    }

    @Test
    void shouldCatchNullPointerWhenKeywordIsNullAndCallCalculateTFIDF() {

        // given
        String keywordNull = null;

        // when
        NullPointerException actualException = assertThrows(NullPointerException.class, () -> searchEngineService.calculateTFIDFValuesFor(keywordNull));

        // then
        assertEquals(actualException.getMessage(), KEYWORD_NULL_ERROR);
    }
}