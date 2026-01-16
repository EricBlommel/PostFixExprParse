package JavaClassFileGenerator;

import symbolTable.SymbolTable;

import java.util.HashMap;
import java.util.Map;

import static JavaClassFileGenerator.JavaClassFileGenerator.*;

public class BytecodeGenerator {
    public final StringBuilder hex = new StringBuilder();
    private final SymbolTable symbolTable;

    // Speichert die Zuordnung: Platzhalter (z.B. X001) -> echter Name (z.B. [globalVar])
    private final Map<String, String> placeholderMap = new HashMap<>();
    private int placeholderCounter = 0;

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

    // Erstellt einen 4-Zeichen Platzhalter für unbekannte Namen und Adressen
    private String createPlaceholder(String substitution) {
        String placeholder = String.format("X%03d", placeholderCounter++);
        placeholderMap.put(placeholder, substitution);
        return placeholder;
    }

    public void push(int v) {
        if (v >= -128 && v <= 127) write(BIPUSH + toByteHex(v));
        else if (v >= -32768 && v <= 32767) write(SIPUSH + toWordHex(v));
        else throw new RuntimeException(v + " ist zu groß/ klein");
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

    public void loadLocal(int index) {
        write(ILOAD + toByteHex(index));
    }

    public void storeLocal(int index) {
        write(ISTORE + toByteHex(index));
    }

    // Globale Variablen benötigen eckige Klammern [] für JCFG
    public void loadGlobal(String name) {
        write(GETSTATIC + createPlaceholder("[" + name + "]"));
    }

    // Globale Variablen benötigen eckige Klammern [] für JCFG
    public void storeGlobal(String name) {
        write(PUTSTATIC + createPlaceholder("[" + name + "]"));
    }

    // Methoden benötigen runde Klammern () für JCFG
    public void writeCall(String methodName, int argCount) {
        write(INVOKESTATIC + createPlaceholder("(" + methodName + ")"));
    }

    public void returnVoid() {
        write(RETURN);
    }

    public void returnInt() {
        write(IRETURN);
    }

    public void print() {
        write(INVOKESTATIC + createPlaceholder("(print)"));
    }

    public void initVar(String n, int v) {
        push(v);
        storeGlobal(n);
    }

    public void initLocalVar(int index, int value) {
        push(value);
        storeLocal(index);
    }

    // Ersetzt alle Platzhalter im Bytecode durch die für den JCFG lesbaren Strings
    private String resolveHex() {
        String finalHex = hex.toString();
        for (Map.Entry<String, String> entry : placeholderMap.entrySet()) {
            finalHex = finalHex.replace(entry.getKey(), entry.getValue());
        }
        return finalHex;
    }

    public String finishMain() {
        write(RETURN);
        return resolveHex();
    }

    public String finish() {
        return resolveHex();
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