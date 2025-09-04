import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Integer> consts = new HashMap<>();

    public void addConstant(String ident, Integer number) throws SymbolAlreadyDefinedException {
        if (consts.containsKey(ident)) {
            throw new SymbolAlreadyDefinedException();
        }
        consts.put(ident, number);
    }

    public Integer getSymbol(String ident) throws UnknownSymbolException {
        if (!consts.containsKey(ident)) {
            throw new UnknownSymbolException();
        }
        return consts.get(ident);
    }
}
