package dev.michel.movementservice.util;

import dev.michel.movementservice.entity.IssuerRegistry;
import dev.michel.movementservice.entity.IssuerRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        calendar.setTime(new Date(timeStamp * 1000));
        return calendar;
    }

    /**
     * Método que convierte un objeto issuerRequest a IssuerRegistry
     *
     * @param issuerRequest El objeto issuerRequest a convertir
     * @return El objeto IssuerRegistry con datos
     */
    public IssuerRegistry issuerRequestToIssuerRegistryMapper(IssuerRequest issuerRequest) {
        IssuerRegistry issuerRegistry = new IssuerRegistry();
        issuerRegistry.setAccountId(issuerRequest.getAccountId());
        issuerRegistry.setOperationMoment(timestampToCalendar(issuerRequest.getTimestamp()).getTime());
        issuerRegistry.setIssuerName(issuerRequest.getIssuerName());
        issuerRegistry.setTotalShares(issuerRequest.getTotal_shares());
        issuerRegistry.setSharePrice(issuerRequest.getShare_price());
        issuerRegistry.setOperation(issuerRequest.getOperation());
        return issuerRegistry;
    }
}
