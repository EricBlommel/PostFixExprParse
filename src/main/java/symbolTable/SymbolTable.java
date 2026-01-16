package symbolTable;

import parser.exceptions.SymbolAlreadyDefinedException;
import parser.exceptions.UnknownSymbolException;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Integer> consts = new HashMap<>();
    // Speichert globale Variablen
    private final Map<String, Integer> vars = new HashMap<>(); // direkt hex speichern Map String String
    private int nextSlot = 1;

    public void addConstant(String ident, Integer number) throws SymbolAlreadyDefinedException {
        if (consts.containsKey(ident) || vars.containsKey(ident)) {
            throw new SymbolAlreadyDefinedException();
        }
        consts.put(ident, number);
    }

    public void addVariable(String ident) throws SymbolAlreadyDefinedException {
        if (consts.containsKey(ident) || vars.containsKey(ident)) {
            throw new SymbolAlreadyDefinedException();
        }
        vars.put(ident, nextSlot++);
    }

    public int constValue(String n) {
        return consts.get(n);
    }

    public boolean isConst(String n) {
        return consts.containsKey(n);
    }

    public boolean isVar(String n) {
        return vars.containsKey(n);
    }

    public Integer getSymbol(String ident) throws UnknownSymbolException {
        if (consts.containsKey(ident)) {
            return consts.get(ident);
        } else if (vars.containsKey(ident)) {
            return vars.get(ident);
        }
        throw new UnknownSymbolException();
    }
}