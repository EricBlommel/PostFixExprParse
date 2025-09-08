package JavaClassFileGenerator;

import symbolTable.SymbolTable;

import static JavaClassFileGenerator.JavaClassFileGenerator.*;

public class CodeGen {
    public final StringBuilder hex = new StringBuilder();
    private final SymbolTable st;

    public CodeGen(SymbolTable st) {
        this.st = st;
    }

    private static String toByteHex(int v) {
        return String.format("%02x", v & 0xFF);
    }

    private static String toWordHex(int v) {
        return String.format("%04x", v & 0xFFFF);
    }

    private StringBuilder emit(String s) {
        return hex.append(s);
    }

    public void emitAt(int atBytes, String fourHex) {
        int i = atBytes * 2;
        hex.replace(i, i + 4, fourHex);
    }

    public void push(int v) {
        if (v >= -128 && v <= 127) emit(BIPUSH + toByteHex(v));       // BIPUSH
        else if (v >= -32768 && v <= 32767) emit(SIPUSH + toWordHex(v)); // SIPUSH
        else {
            throw new IllegalArgumentException(v + " ist zu groß/ klein für bipush/ sipush");
        }
    }

    public void load(String name) {
        if (st.isConst(name)) push(st.constValue(name));
        else emit(ILOAD + toByteHex(st.varSlot(name)));
    }

    public void store(String name) {
        emit(ISTORE + toByteHex(st.varSlot(name)));
    }

    public void add() {
        emit(IADD);
    }

    public void sub() {
        emit(ISUB);
    }

    public void mul() {
        emit(IMUL);
    }

    public void div() {
        emit(IDIV);
    }

    public void print() {
        emit(INVOKESTATIC + "ZZZZ");
    }

    public void initVar(String n, int v) {
        push(v);
        store(n);
    }

    public String finishMain() {
        return emit(RETURN).toString().replace("ZZZZ", "(print)");
    }

    public int pc() {
        return hex.length() / 2;
    }

    public int emitJumpWithPlaceholder(String op) {
        emit(op + "0000");
        return pc() - 2;
    }

    public void patchJumpPlaceholder(int at, int target) {
        int rel = target - (at - 1);
        emitAt(at, toWordHex(rel));
    }

    public int if_icmpeq() {
        return emitJumpWithPlaceholder(IF_ICMPEQ);
    }

    public int if_icmpne() {
        return emitJumpWithPlaceholder(IF_ICMPNE);
    }

    public int if_icmplt() {
        return emitJumpWithPlaceholder(IF_ICMPLT);
    }

    public int if_icmpge() {
        return emitJumpWithPlaceholder(IF_ICMPGE);
    }

    public int if_icmpgt() {
        return emitJumpWithPlaceholder(IF_ICMPGT);
    }

    public int if_icmple() {
        return emitJumpWithPlaceholder(IF_ICMPLE);
    }

    public int ifFalse_goto() {
        return emitJumpWithPlaceholder(GOTO);
    }

}
