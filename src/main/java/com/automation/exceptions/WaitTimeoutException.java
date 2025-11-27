package com.automation.exceptions;

/**
 * Excecao customizada para timeouts de espera.
 *
 * Arquitetura:
 * - Extends RuntimeException para evitar checked exceptions
 * - Mensagens descritivas sobre o elemento/condicao
 * - Preserva stack trace original
 */
public class WaitTimeoutException extends RuntimeException {

    public WaitTimeoutException(String message) {
        super(message);
    }

    public WaitTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
