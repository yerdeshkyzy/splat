package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;
import java.util.Objects;
public class BinOp extends Expression{

    Token tok, operator;
    private Expression left, right;
    public BinOp (Token tok, Expression left, Token operator, Expression right) {
        super(tok);
        this.left = left;
        this.operator = operator;
        this.right = right;

    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "( " + left.toString() + " " + operator.toString() + " " + right.toString() + " )\n";
    }

    @Override
    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {

        Type temp1 = getLeft().analyzeAndGetType(funcMap,varAndParamMap);
        Type temp2 = getRight().analyzeAndGetType(funcMap,varAndParamMap);

        String binOp = operator.getValue();
        if (!(Objects.equals(temp1.toString(), temp2.toString()))){
            throw new SemanticAnalysisException("Binary oparation error: incompatible types",left);
        } else {
            if (binOp.equals("and") || binOp.equals("or")) {
                if (!temp1.toString().equals("Boolean")) {
                    throw new SemanticAnalysisException("Binary operation error",right);
                }
                else {
                    return temp1;
                }
            } else if (binOp.equals("==")) {
                Type binType = new Type(new Token("Boolean",0,0));
                return binType;
            }  else if (binOp.equals("+")) {
                if (temp1.toString().equals("Boolean")) {
                    throw new SemanticAnalysisException("Binary operation error",left);
                }
                else {
                    return temp1;
                }
            } else {
                if (!temp1.toString().equals("Integer")) {
                    throw new SemanticAnalysisException("Binary operation error",left);
                } else {
                    if (binOp.toCharArray()[0] == 60 || binOp.toCharArray()[0] == 62) {
                        Type binType = new Type(new Token("Boolean",0,0));
                        return binType;
                    } else {
                        return temp1;
                    }
                }
            }

        }

    }
}
