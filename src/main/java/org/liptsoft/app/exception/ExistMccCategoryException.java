package org.liptsoft.app.exception;

public class ExistMccCategoryException extends RuntimeException {

    public ExistMccCategoryException(String categoryName, Integer mcc) {
        super(String.format("MCC \"%d\" уже зарезервирован для категории \"%s\"", mcc, categoryName));
    }

}
