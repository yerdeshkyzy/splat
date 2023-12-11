package splat.lexer;

public class Token {
    // create three fields
    private String value;
    private int line, column;

    public Token(String val, int ln, int cn ) {
        // set fields
        value = val;
        line = ln;
        column = cn;
    }

    public String getValue() {
        return value;
    }
    public int getLine() {
        return line;
    }
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return value;
    }


}
