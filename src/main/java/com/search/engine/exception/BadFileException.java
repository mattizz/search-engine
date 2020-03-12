package com.search.engine.exception;

public class BadFileException extends RuntimeException {

    public static final String FILE_NULL_ERROR = "File can't be null";
    public static final String EMPTY_FILE_ERROR = "File is empty";
    public static final String FILENAME_ERROR = "Bad filename: ";
    public static final String READ_FILE_ERROR = "Can't read file";
    public static final String SAVE_FILE_ERROR = "Can't save file ";

    public BadFileException(String s) {
        super(s);
    }
}
