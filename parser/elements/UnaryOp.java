package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class UnaryOp extends Expression{
    private Expression expr;
    private Token operator;
    public UnaryOp(Token tok, Token operator, Expression expr) {
        super(tok);
        this.expr = expr;
        this.operator = operator;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type temp;
        temp = getExpr().analyzeAndGetType(funcMap,varAndParamMap);
        if (operator.getValue().equals("not")) {

            if (!temp.toString().equals("Boolean")) {
                throw new SemanticAnalysisException("Binary operation error",expr);
            } else {
                return temp;
            }
        } else {
            if (!temp.toString().equals("Integer")) {
                throw new SemanticAnalysisException("Binary operation error",expr);
            } else {
                return temp;
            }
        }

    }


}
