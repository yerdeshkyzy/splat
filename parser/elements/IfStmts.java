package splat.parser.elements;

import splat.lexer.Token;
import java.util.List;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.List;
import java.util.Map;
public class IfStmts extends Statement{
    private Expression expr;
    private List<Statement> ifstmts, elsestmts;


//    public IfStmts(Token tok, Expression expr,
//                   List<Statement> ifstmts ) {
//        super(tok);
//        this.expr = expr;
//        this.ifstmts = ifstmts;
//
//    }
    public IfStmts(Token tok, Expression expr,
                   List<Statement> ifstmts, List<Statement> elsestmts ) {
        super(tok);
        this.expr = expr;
        this.ifstmts = ifstmts;
        this.elsestmts = elsestmts;

    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getIfstmts() {
        return ifstmts;
    }

    public List<Statement> getElsestmts() {
        return elsestmts;
    }

    @Override
    public String toString() {
        return "If(" + expr.toString() +") then " + ifstmts.toString() + "\n + Else " + elsestmts.toString() + "end if;";
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type tf = expr.analyzeAndGetType(funcMap, varAndParamMap);
        if (!tf.toString().equals("Boolean")) {
            throw new SemanticAnalysisException("Provide boolean",expr);
        }
        for (Statement stmt : ifstmts) {
            stmt.analyze(funcMap,varAndParamMap);
        }
        if (!(elsestmts == null)) {
            for (Statement stmt : elsestmts) {
                stmt.analyze(funcMap,varAndParamMap);
            }
        }

    }

}
