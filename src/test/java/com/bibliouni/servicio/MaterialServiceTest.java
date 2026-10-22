package com.bibliouni.servicio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba inicial del entorno de pruebas de BiblioUni.
 * Comprueba que el contexto de Spring arranca correctamente.
 *
 * Nota: @SpringBootTest busca la clase @SpringBootApplication subiendo por los
 * paquetes; funciona si la clase principal esta en com.bibliouni.
 */
@SpringBootTest
class MaterialServiceTest {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring cargue correctamente
    }
}
