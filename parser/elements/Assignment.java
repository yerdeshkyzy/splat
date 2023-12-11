package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.*;
public class Assignment extends Statement{
    private Token label;
    private Expression expr;
    public Assignment(Token tok,  Expression expr) {
        super(tok);
        this.label = tok;
        this.expr = expr;

    }

    public Token getLabel() {
        return label;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return label.toString() + " = " + expr.toString() +"\n";
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!varAndParamMap.containsKey(getLabel().getValue())) {
            throw new SemanticAnalysisException("No variable to assign ", label.getLine(), label.getColumn());
        } else {
            Type assignmentType = varAndParamMap.get(getLabel().getValue());
            if (!Objects.equals(assignmentType.toString(), expr.analyzeAndGetType(funcMap, varAndParamMap).toString())) {
                throw new SemanticAnalysisException("Assignment type is incorrect", label.getLine(), label.getColumn());
            }
        }
    }



}
