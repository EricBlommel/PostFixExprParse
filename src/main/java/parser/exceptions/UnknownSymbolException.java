package parser.exceptions;

public class UnknownSymbolException extends RuntimeException {
    public UnknownSymbolException() {
        super("Unbekanntes Symbol");
    }
}
