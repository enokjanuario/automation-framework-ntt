package com.automation.models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Modelo que representa uma resposta de erro da API.
 *
 * Arquitetura:
 * - Padroniza tratamento de erros de API
 * - Permite deserializacao de diferentes formatos de erro
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error")
    private String error;

    @JsonProperty("path")
    private String path;

    @JsonProperty("timestamp")
    private String timestamp;

    public ApiError() {
    }

    public ApiError(Integer status, String message, String error, String path, String timestamp) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = timestamp;
    }

    // Getters
    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Verifica se a resposta indica erro de recurso nao encontrado.
     *
     * @return true se for erro 404
     */
    public boolean isNotFound() {
        return status != null && status == 404;
    }

    /**
     * Verifica se a resposta indica erro de requisicao invalida.
     *
     * @return true se for erro 400
     */
    public boolean isBadRequest() {
        return status != null && status == 400;
    }

    public static ApiErrorBuilder builder() {
        return new ApiErrorBuilder();
    }

    /**
     * Builder class for ApiError.
     */
    public static class ApiErrorBuilder {
        private Integer status;
        private String message;
        private String error;
        private String path;
        private String timestamp;

        ApiErrorBuilder() {
        }

        public ApiErrorBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public ApiErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ApiErrorBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ApiErrorBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiError build() {
            return new ApiError(status, message, error, path, timestamp);
        }
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", path='" + path + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return Objects.equals(status, apiError.status) &&
                Objects.equals(message, apiError.message) &&
                Objects.equals(error, apiError.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, error);
    }
}
