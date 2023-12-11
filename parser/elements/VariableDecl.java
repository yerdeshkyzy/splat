package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

    // Need to add some fields


    // Need to add extra arguments for setting fields in the constructor
    public VariableDecl(Token tok, Token label, Type type) {
        super(tok, label, type);

    }

    // Getters


    // Fix this as well
    public String toString() {
        return getLabel().toString()+" : "+getType().toString()+";\n";
    }


}
