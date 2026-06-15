package cat.itacademy.s04.t01.userapi.level2andLevel3.exceptions;

public record ErrorResponse(
        String message,
        int status,
        String error
) { }
