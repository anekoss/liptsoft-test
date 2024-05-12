package org.liptsoft.app.exception;

public class NoExistMccCategoryException extends RuntimeException {

    public NoExistMccCategoryException(Integer mcc) {
        super(String.format("Категория с mcc \"%d\" не найдена", mcc));
    }

}
