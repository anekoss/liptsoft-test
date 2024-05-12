package org.liptsoft.app.exception;

public class NotFoundTransactionException extends RuntimeException {

    public NotFoundTransactionException() {
        super("Транзакция не найдена");
    }
}
