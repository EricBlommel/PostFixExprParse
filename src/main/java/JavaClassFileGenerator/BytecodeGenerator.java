package JavaClassFileGenerator;

import symbolTable.SymbolTable;

import static JavaClassFileGenerator.JavaClassFileGenerator.*;

public class BytecodeGenerator {
    public final StringBuilder hex = new StringBuilder();
    private final SymbolTable symbolTable;

    public BytecodeGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private static String toByteHex(int v) {
        return String.format("%02x", v & 0xFF);
    }

    private static String toWordHex(int v) {
        return String.format("%04x", v & 0xFFFF);
    }

    private StringBuilder write(String s) {
        return hex.append(s);
    }

    public void writeAt(int atBytes, String fourHex) {
        int i = atBytes * 2;
        hex.replace(i, i + 4, fourHex);
    }

    public void push(int v) {
        if (v >= -128 && v <= 127) write(BIPUSH + toByteHex(v));       // BIPUSH
        else if (v >= -32768 && v <= 32767) write(SIPUSH + toWordHex(v)); // SIPUSH
        else {
            throw new IllegalArgumentException(v + " ist zu groß/ klein für bipush/ sipush");
        }
    }

    public void load(String name) {
        if (symbolTable.isConst(name)) push(symbolTable.constValue(name));
        else write(ILOAD + toByteHex(symbolTable.varSlot(name)));
    }

    public void store(String name) {
        write(ISTORE + toByteHex(symbolTable.varSlot(name)));
    }

    public void add() {
        write(IADD);
    }

    public void sub() {
        write(ISUB);
    }

    public void mul() {
        write(IMUL);
    }

    public void div() {
        write(IDIV);
    }

    public void print() {
        write(INVOKESTATIC + "ZZZZ");
    }

    public void initVar(String n, int v) {
        push(v);
        store(n);
    }

    public String finishMain() {
        return write(RETURN).toString().replace("ZZZZ", "(print)");
    }

    public int pc() {
        return hex.length() / 2;
    }

    public int writeJumpWithPlaceholder(String op) {
        write(op + "0000");
        return pc() - 2;
    }

    public void patchJumpPlaceholder(int at, int target) {
        int rel = target - (at - 1);
        writeAt(at, toWordHex(rel));
    }

    public int if_icmpeq() {
        return writeJumpWithPlaceholder(IF_ICMPEQ);
    }

    public int if_icmpne() {
        return writeJumpWithPlaceholder(IF_ICMPNE);
    }

    public int if_icmplt() {
        return writeJumpWithPlaceholder(IF_ICMPLT);
    }

    public int if_icmpge() {
        return writeJumpWithPlaceholder(IF_ICMPGE);
    }

    public int if_icmpgt() {
        return writeJumpWithPlaceholder(IF_ICMPGT);
    }

    public int if_icmple() {
        return writeJumpWithPlaceholder(IF_ICMPLE);
    }

    public int ifFalse_goto() {
        return writeJumpWithPlaceholder(GOTO);
    }

}
