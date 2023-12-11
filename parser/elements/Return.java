package splat.parser.elements;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import javax.swing.*;
import java.util.Map;

public class Return extends Statement{
    private Expression expr;
    String whichReturn = "";
    private Token tok;
    public Return (Token tok) {
        super(tok);
        this.tok = tok;
        whichReturn = "void";
    }
    public  Return (Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
        this.tok = tok;
        whichReturn = "expr";
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (whichReturn.equals("expr")) {
            if(!varAndParamMap.get("return").toString().equals(expr.analyzeAndGetType(funcMap, varAndParamMap).toString())) {
                throw new SemanticAnalysisException("Return Error", expr);
            }
        } else {
            if(!varAndParamMap.containsKey("return")) {
                throw new SemanticAnalysisException("Return is incorrect",tok.getLine(),tok.getColumn());
            } else {
                if (!varAndParamMap.get("return").toString().equals("void")) {
                    throw new SemanticAnalysisException("Return is incorrect", tok.getLine(), tok.getColumn());
                }
            }

        }

    }
}
