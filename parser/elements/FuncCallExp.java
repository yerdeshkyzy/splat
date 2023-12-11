package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.*;

public class FuncCallExp extends Expression{
    private Token label;
    private List<Expression> args;

    public FuncCallExp(Token tok){
        super(tok);
        label = tok;

    }
    public FuncCallExp(Token tok, List<Expression> args){
        super(tok);
        label = tok;
        this.args = args;
    }

    public Token getLabel() {
        return label;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return  "( "+ label.toString() +
                args.toString() +
                " )";
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if (!funcMap.containsKey(getLabel().toString())) {
            throw new SemanticAnalysisException("Function call expression is incorrect", label.getLine(), label.getColumn());
        } else {
            FunctionDecl func = funcMap.get(getLabel().toString());
            Set<String> expType = new HashSet<>();
            for (Expression exp : args){
                expType.add(exp.analyzeAndGetType(funcMap,varAndParamMap).toString());
            }
            Set<String> paramType = new HashSet<String>();
            for (VariableDecl decl : func.getParams()) {
                paramType.add(decl.getType().toString());
            }

            for ( int i=0; i < expType.size();i++) {
                if(!expType.toArray()[i].equals(paramType.toArray()[i])){
                    throw new SemanticAnalysisException("Function call error", label.getLine(), label.getColumn());
                }
            }
            return func.getType();
        }

    }

}
