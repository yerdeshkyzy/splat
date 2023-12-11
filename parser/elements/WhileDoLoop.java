package splat.parser.elements;

import splat.lexer.Token;
import java.util.List;
import java.util.Map;
import splat.semanticanalyzer.SemanticAnalysisException;
public class WhileDoLoop extends Statement{
    private Expression expr;
    private List<Statement> stmts;

    public WhileDoLoop(Token tok, Expression expr, List<Statement> stmts) {
        super(tok);
        this.expr = expr;
        this.stmts = stmts;

    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmts() {
        return stmts;
    }
    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type b = expr.analyzeAndGetType(funcMap, varAndParamMap);
        if (!(b.toString().equals("Boolean"))) {
            throw new SemanticAnalysisException("Provide boolean value",expr);
        }
        for (Statement stmt : stmts) {
            stmt.analyze(funcMap,varAndParamMap);
        }
    }
}
