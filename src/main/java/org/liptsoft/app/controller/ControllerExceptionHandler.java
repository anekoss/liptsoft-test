package org.liptsoft.app.controller;

import model.ApiErrorResponse;
import org.liptsoft.app.exception.ExistCategoryNameException;
import org.liptsoft.app.exception.ExistMccCategoryException;
import org.liptsoft.app.exception.NoExistCategoryNameException;
import org.liptsoft.app.exception.NoExistMccCategoryException;
import org.liptsoft.app.exception.NotFoundTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ExistCategoryNameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Имя категории уже существуют")
    public ApiErrorResponse existCategoryNameException(ExistCategoryNameException exception, WebRequest webRequest) {
        return new ApiErrorResponse().exceptionMessage(exception.getMessage());
    }

    @ExceptionHandler(ExistMccCategoryException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "MCC принадлежит другой категории")
    public ApiErrorResponse existMccException(ExistMccCategoryException exception, WebRequest webRequest) {
        return new ApiErrorResponse().exceptionMessage(exception.getMessage());
    }

    @ExceptionHandler(NoExistCategoryNameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Категория с таким именем не существует")
    public ApiErrorResponse noExistCategoryNameException(
        NoExistCategoryNameException exception,
        WebRequest webRequest
    ) {
        return new ApiErrorResponse().exceptionMessage(exception.getMessage());
    }

    @ExceptionHandler(NoExistMccCategoryException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Категория с таким MCC не существует")
    public ApiErrorResponse noExistMccCategoryException(NoExistMccCategoryException exception, WebRequest webRequest) {
        return new ApiErrorResponse().exceptionMessage(exception.getMessage());
    }

    @ExceptionHandler(NotFoundTransactionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Категория с таким MCC не существует")
    public ApiErrorResponse notFoundTransactionException(
        NotFoundTransactionException exception,
        WebRequest webRequest
    ) {
        return new ApiErrorResponse().exceptionMessage(exception.getMessage());
    }

}
