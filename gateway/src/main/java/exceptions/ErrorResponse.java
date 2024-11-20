package exceptions;

public record ErrorResponse(
        int statusCode,
        String message
) {
}
