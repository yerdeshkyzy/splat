package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.*;
import java.util.List;

public class FuncCall extends Statement{
    private List<Expression> expression;
    private Token funcLabel;
//    public FuncCall( Token tok) {
//        super(tok);
//    }
    public FuncCall( Token tok, List<Expression> expr) {
        super(tok);
        this.expression = expr;
        this.funcLabel = tok;
    }

    public List<Expression> getExpression() {
        return expression;
    }

    public Token getFuncLabel() {
        return funcLabel;
    }
    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!funcMap.containsKey(getFuncLabel().getValue())){
            throw new SemanticAnalysisException("Incorrect function call", funcLabel.getLine(), funcLabel.getColumn());
        } else {
            FunctionDecl func = funcMap.get(getFuncLabel().getValue());
            Set<String> expType = new HashSet<String>();
            for (Expression exp : expression){
                expType.add(exp.analyzeAndGetType(funcMap,varAndParamMap).toString());
            }
            Set<String> paramType = new HashSet<String>();
            for (VariableDecl decl : func.getParams()) {
                paramType.add(decl.getType().toString());
            }

            for ( int i=0; i < expType.size();i++) {
                if(!expType.toArray()[i].equals(paramType.toArray()[i])){
                    throw new SemanticAnalysisException("Error while calling function ", funcLabel.getLine(), funcLabel.getColumn());
                }
            }
        }


    }

}
