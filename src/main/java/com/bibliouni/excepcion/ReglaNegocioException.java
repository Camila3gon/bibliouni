package com.bibliouni.excepcion;

/** Se lanza cuando se viola una regla de negocio (HTTP 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
