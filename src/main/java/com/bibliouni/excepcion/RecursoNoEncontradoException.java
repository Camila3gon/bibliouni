package com.bibliouni.excepcion;

/** Se lanza cuando un usuario, material o préstamo no existe (HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
