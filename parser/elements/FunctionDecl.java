package splat.parser.elements;

import splat.lexer.Token;
import java.util.List;

public class FunctionDecl extends Declaration {

    // Need to add some fields
    private List<Statement> stmts;
    private List<VariableDecl> params;
    private List<VariableDecl> localVars;




    // Need to add extra arguments for setting fields in the constructor
    public FunctionDecl(Token tok, Token label, List<VariableDecl> params, Type returnType,
                        List<VariableDecl> localVars, List<Statement> stmts) {
        super(tok, label, returnType);
        this.params = params;
        this.localVars = localVars;
        this.stmts = stmts;

    }

    // Getters
    public List<Statement> getStmts() {
        return stmts;
    }

    public List<VariableDecl> getParams() {
        return params;
    }

    public List<VariableDecl> getLocalVars() {
        return localVars;
    }





    // Fix this as well
    public String toString() {
        return  getLabel()+ "("+ params.toString()+ ") :"
        + getType().toString()+ " is "+ localVars.toString()
        + "begin \n" + stmts.toString()+ "end; \n";
    }


}
