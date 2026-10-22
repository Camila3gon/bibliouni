package com.bibliouni.excepcion;

/** Se lanza cuando el rol no tiene permiso para la operación (HTTP 403). */
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
