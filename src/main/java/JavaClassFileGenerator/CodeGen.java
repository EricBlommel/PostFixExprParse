package JavaClassFileGenerator;

import symbolTable.SymbolTable;

public class CodeGen {
    private final StringBuilder hex = new StringBuilder();
    private final SymbolTable st;

    public CodeGen(SymbolTable st) {
        this.st = st;
    }

    private static String b(int v) {
        return String.format("%02x", v & 0xFF);
    }

    private static String w(int v) {
        return String.format("%04x", v & 0xFFFF);
    }

    private void emit(String s) {
        hex.append(s);
    }

    public void pushInt(int v) {
        if (v >= -128 && v <= 127) emit("10" + b(v));       // BIPUSH
        else if (v >= -32768 && v <= 32767) emit("11" + w(v)); // SIPUSH
        else {
            int hi = v >>> 16, lo = v & 0xFFFF;
            pushInt(hi);
            pushInt(65536);
            mul();
            pushInt(lo);
            add();
        }
    }

    public void load(String name) {
        if (st.isConst(name)) pushInt(st.constValue(name));
        else emit("15" + b(st.varSlot(name)));
    } // ILOAD

    void store(String name) {
        emit("36" + b(st.varSlot(name)));
    } // ISTORE

    public void add() {
        emit("60");
    }

    public void sub() {
        emit("64");
    }

    public void mul() {
        emit("68");
    }

    public void div() {
        emit("6c");
    }

    public void print() {
        emit("b8(print)");
    } // invokestatic print(I)V – Platzhalter, API ersetzt (print) automatisch

    public void initVar(String n, int v) {
        pushInt(v);
        store(n);
    }

    public String finishMain() {
        return hex.append("b1").toString();
    } // RETURN
}
