package org.liptsoft.app.exception;

public class NoExistCategoryNameException extends RuntimeException {

    public NoExistCategoryNameException(String categoryName) {
        super(String.format("Категория с именем \"%s\" не найдена", categoryName));
    }

}
