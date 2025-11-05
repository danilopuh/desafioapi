package br.com.desafioapi.utils;

import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(AllureUtils.class);
    
    @Attachment(value = "Response Body", type = "application/json")
    public static String attachResponseBody(Response response) {
        String responseBody = response.getBody().asString();
        logger.debug("Anexando response body ao relatório Allure");
        return responseBody;
    }
    
    @Attachment(value = "Request Body", type = "application/json")
    public static String attachRequestBody(String requestBody) {
        logger.debug("Anexando request body ao relatório Allure");
        return requestBody;
    }
    
    @Attachment(value = "Response Headers", type = "text/plain")
    public static String attachResponseHeaders(Response response) {
        StringBuilder headers = new StringBuilder();
        response.getHeaders().forEach(header -> 
            headers.append(header.getName()).append(": ").append(header.getValue()).append("\n")
        );
        return headers.toString();
    }
    
    @Attachment(value = "Test Context", type = "text/plain")
    public static String attachTestContext(String context) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return String.format("Timestamp: %s\nContext: %s", timestamp, context);
    }
    
    @Attachment(value = "Error Details", type = "text/plain")
    public static String attachErrorDetails(Exception exception) {
        StringBuilder errorDetails = new StringBuilder();
        errorDetails.append("Exception: ").append(exception.getClass().getSimpleName()).append("\n");
        errorDetails.append("Message: ").append(exception.getMessage()).append("\n");
        errorDetails.append("Stack Trace:\n");
        
        for (StackTraceElement element : exception.getStackTrace()) {
            errorDetails.append(element.toString()).append("\n");
        }
        
        return errorDetails.toString();
    }
}