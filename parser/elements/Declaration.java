package splat.parser.elements;

import splat.lexer.Token;

public abstract class Declaration extends ASTElement {
    private Token label;
    private Type type;
    public Declaration(Token tok, Token label, Type type) {
        super(tok);
        this.label = label;
        this.type = type;
    }

    public Token getLabel() {
        return label;
    }
    public Type getType(){
        return type;
    }
}
