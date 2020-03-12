package com.search.engine.service;

import com.search.engine.entity.DocumentInfo;
import com.search.engine.entity.Posting;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class SearchEngineService {

    public static final String EMPTY_STRING = "";
    public static final String SPACE_SEPARATOR = " ";
    public static final String SPECIAL_CHARS = "[-+.^:,?!]";
    public static final String TEXT_NULL_MESSAGE = "Text can'be null";
    public static final String FILENAME_NULL_MESSAGE = "Filename can't be null";
    public static final String WORD_NULL_MESSAGE = "Word can't be null";

    private final Map<String, Posting> wordsDictionary = new TreeMap<>();
    private int numberOfProcessedDocument = 0;

    public void createInvertedIndexStructure(String text, String documentName) {
        requireNonNull(text, TEXT_NULL_MESSAGE);
        requireNonNull(text, FILENAME_NULL_MESSAGE);

        List<String> wordsInDocument = getTransformedListWithWords(text);

        for (String word : wordsInDocument) {
            if (wordsDictionary.containsKey(word)) {
                addNewInfoToExistWord(documentName, word, wordsInDocument);
            } else {
                addNewWordToDictionary(documentName, word, wordsInDocument);
            }
        }

        numberOfProcessedDocument++;
    }

    private List<String> getTransformedListWithWords(String text) {
        String textWithoutSpecialChars = text.replaceAll(SPECIAL_CHARS, EMPTY_STRING);
        String[] textAfterSplit = textWithoutSpecialChars.split(SPACE_SEPARATOR);

        return Arrays.stream(textAfterSplit)
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private void addNewInfoToExistWord(String documentName, String word, List<String> wordsInDocument) {
        Posting posting = wordsDictionary.get(word);

        boolean isWordAppearedInThisDocumentBefore = posting.getDocuments().containsKey(documentName);

        if (isWordAppearedInThisDocumentBefore) {
            updateWordsFrequencyInDocument(documentName, posting);
        } else {
            addNewDocumentInfo(documentName, posting);
        }

        updateParamTF(documentName, wordsInDocument, posting);
    }

    private void addNewDocumentInfo(String documentName, Posting posting) {
        DocumentInfo documentInfo = new DocumentInfo();
        posting.getDocuments().put(documentName, documentInfo);
    }

    private void updateParamTF(String documentName, List<String> wordsInDocument, Posting posting) {
        int wordFrequency = posting.getDocuments().get(documentName).getWordFrequencyInDocument();
        int totalWords = wordsInDocument.size();
        double paramTF = (double) wordFrequency / totalWords;
        posting.getDocuments().get(documentName).setTermFrequency(paramTF);
    }

    private void updateWordsFrequencyInDocument(String documentName, Posting posting) {
        int wordFrequencyInDocument = posting.getDocuments().get(documentName).getWordFrequencyInDocument();
        posting.getDocuments().get(documentName).setWordFrequencyInDocument(wordFrequencyInDocument + 1);
    }

    private void addNewWordToDictionary(String documentName, String word, List<String> wordsInDocument) {
        Posting posting = new Posting();
        DocumentInfo documentInfo = new DocumentInfo();
        posting.getDocuments().put(documentName, documentInfo);
        updateParamTF(documentName, wordsInDocument, posting);
        wordsDictionary.put(word, posting);
    }

    public Set<String> getDocumentsContaining(String keyword) {
        requireNonNull(keyword, WORD_NULL_MESSAGE);

        String searchWord = keyword.toLowerCase().trim();
        return wordsDictionary.containsKey(searchWord) ? getDocumentsFor(searchWord) : Collections.emptySet();
    }

    private Set<String> getDocumentsFor(String searchWord) {
        Posting posting = wordsDictionary.get(searchWord);
        return posting.getDocuments().keySet();
    }

    public Map<String, Double> calculateTFIDFValuesFor(String keyword) {
        requireNonNull(keyword, WORD_NULL_MESSAGE);

        String searchKeyword = keyword.toLowerCase();
        return wordsDictionary.containsKey(searchKeyword) ? calculateTFIDF(searchKeyword) : Collections.emptyMap();
    }

    private Map<String, Double> calculateTFIDF(String word) {
        Map<String, Double> result = new HashMap<>();

        Map<String, DocumentInfo> documents = wordsDictionary.get(word).getDocuments();
        int documentsNumberContainingWord = documents.size();
        double paramIDF = Math.log((double) numberOfProcessedDocument / documentsNumberContainingWord);

        for (Map.Entry<String, DocumentInfo> map : documents.entrySet()) {
            String documentName = map.getKey();
            double paramTF = map.getValue().getTermFrequency();
            result.put(documentName, paramTF * paramIDF);
        }

        return result;
    }
}
