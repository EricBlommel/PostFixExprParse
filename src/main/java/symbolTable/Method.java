package symbolTable;

import JavaClassFileGenerator.BytecodeGenerator;
import parser.exceptions.SymbolAlreadyDefinedException;

import java.util.HashMap;
import java.util.Map;

public class Method {
    private String name;
    private boolean isFunction;
    private int paramCount;

    // Speichert lokale Variablen und Parameter
    private Map<String, Integer> localVars = new HashMap<>();
    private int currentLocalVarIndex = 0;

    // Jede Methode hat eigenen BytecodeGenerator
    private BytecodeGenerator code;

    public Method(String name, boolean isFunction, SymbolTable globalTable) {
        this.name = name;
        this.isFunction = isFunction;
        this.code = new BytecodeGenerator(globalTable);
    }

    public void addParameter(String paramName) {
        if (localVars.containsKey(paramName)) {
            throw new RuntimeException("Parameter " + paramName + " bereits definiert!");
        }
        localVars.put(paramName, currentLocalVarIndex++);
        paramCount++;
    }

    public void addLocalVariable(String varName) {
        if (localVars.containsKey(varName)) {
            throw new SymbolAlreadyDefinedException();
        }
        localVars.put(varName, currentLocalVarIndex++);
    }

    public int getVarIndex(String varName) {
        if (!localVars.containsKey(varName)) {
            return -1; // Nicht lokal gefunden -> global suchen
        }
        return localVars.get(varName);
    }

    public BytecodeGenerator getCode() {
        return code;
    }

    public String getName() { return name; }
    public boolean isFunction() { return isFunction; }
    public int getParamCount() { return paramCount; }
}