package com.bibliouni.dto;

import java.util.List;

public record ErrorResponse(int estado, String mensaje, List<String> detalles) {
}
