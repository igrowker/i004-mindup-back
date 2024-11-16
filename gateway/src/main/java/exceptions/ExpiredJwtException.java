package exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExpiredJwtException extends RuntimeException {
    private String message;
}
