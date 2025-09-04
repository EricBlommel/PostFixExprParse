public class SymbolAlreadyDefinedException extends RuntimeException {
    public SymbolAlreadyDefinedException() {
        super("Symbol existiert bereits");
    }
}
