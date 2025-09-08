package symbolTable;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Integer> consts = new HashMap<>();
    private final Map<String, Integer> vars = new HashMap<>();
    private int nextSlot = 1; // Slot 0 = args

    public void addConstant(String ident, Integer number) throws SymbolAlreadyDefinedException {
        if (consts.containsKey(ident) || vars.containsKey(ident)) {
            throw new SymbolAlreadyDefinedException();
        }
        consts.put(ident, number);
    }

    public void addVariable(String ident, Integer slot) {
        if (consts.containsKey(ident) || vars.containsKey(ident)) {
            throw new SymbolAlreadyDefinedException();
        }
        vars.put(ident, slot);
    }

    public int addVariable(String ident) {
        int slot = nextSlot++;
        addVariable(ident, slot);
        return slot;
    }

    public Integer getSymbol(String ident) throws UnknownSymbolException {
        if (!consts.containsKey(ident)) {
            throw new UnknownSymbolException();
        }
        return consts.get(ident);
    }

    public boolean isConst(String n) {
        return consts.containsKey(n);
    }

    public boolean isVar(String n) {
        return vars.containsKey(n);
    }

    public int constValue(String n) {
        return consts.get(n);
    }

    public int varSlot(String n) {
        return vars.get(n);
    }
}
