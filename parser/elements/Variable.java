package splat.parser.elements;

import splat.lexer.Token;
import java.util.Map;
public class Variable extends Expression{
    private Token tok;
    private String label;

    public Variable(Token tok) {
        super(tok);
        this.tok = tok;
        this.label = tok.getValue();
    }


    @Override
    public String toString() {
        return  label;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) {
        Type varType;
        if (varAndParamMap.containsKey(label)) {
            varType = varAndParamMap.get(label);
        } else {
            if (label.equals("true") || label.equals("false")) {
                varType = new Type(new Token("Boolean",0,0));
            } else {
                int c = label.toCharArray()[0];
                if ( c >= 48 && c <= 57) {
                    varType = new Type(new Token("Integer",0,0));
                } else {
                    varType = new Type(new Token("String",0,0));
                }
            }
        }

        return varType;
    }

}
