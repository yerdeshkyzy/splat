package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.List;
import java.util.Map;

public class Print extends Statement {
    private Expression expr;

    public Print( Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        getExpr().analyzeAndGetType(funcMap,varAndParamMap);
    }
}
