package org.liptsoft.app.exception;

public class ExistMccCategoryException extends Exception {
    private final String message;

    public ExistMccCategoryException(String categoryName, String mcc) {
        super();
        this.message = String.format("MCC %s уже зарезервирован для категории \"%s\"", mcc, categoryName);
    }

}
