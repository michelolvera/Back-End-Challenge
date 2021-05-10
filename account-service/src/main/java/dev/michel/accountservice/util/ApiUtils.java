package dev.michel.accountservice.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Clase que almacena métodos útiles para el funcionamiento de la API
 */
public class ApiUtils {
    /**
     * Método que permite convertir excepciones de validación en una cadena humanamente legible
     *
     * @param result Resultado de validaciones
     * @return Cadena de texto que se entrega en la respuesta de la API
     */
    public String formatMessage(BindingResult result) {
        StringBuilder errorsText = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            errorsText.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(". ");
        }
        return errorsText.substring(0, errorsText.length() - 1);
    }
}
