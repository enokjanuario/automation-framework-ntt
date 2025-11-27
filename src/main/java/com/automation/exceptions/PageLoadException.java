package com.automation.exceptions;

/**
 * Excecao customizada para erros de carregamento de pagina.
 */
public class PageLoadException extends RuntimeException {

    public PageLoadException(String message) {
        super(message);
    }

    public PageLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
