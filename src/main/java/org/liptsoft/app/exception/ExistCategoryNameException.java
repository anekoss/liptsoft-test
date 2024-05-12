package org.liptsoft.app.exception;

public class ExistCategoryNameException extends RuntimeException {

    public ExistCategoryNameException(String categoryName) {
        super(String.format("Категория с именем \"%s\" уже существует", categoryName));
    }

}
