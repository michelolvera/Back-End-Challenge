package dev.michel.movementservice.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Calendar;
import java.util.Date;

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

    public Calendar timestampToCalendar(Long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp * 1000));
        return calendar;
    }
}
