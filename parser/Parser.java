package splat.parser;


import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;

public class Parser {

    private List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Compares the next token to an expected value, and throws
     * an exception if they don't match.  This removes the front-most
     * (next) token
     *
     * @param expected value of the next token
     * @throws ParseException if the actual token doesn't match what
     * 			was expected
     */
    private void checkNext(String expected) throws ParseException {

        Token tok = tokens.remove(0);

        if (!tok.getValue().equals(expected)) {
            throw new ParseException("Expected '"+ expected + "' line "+tok.getLine()+" column "+tok.getColumn() + ", got '"
                    + tok.getValue()+ "'.", tok);
        }
    }

    /**
     * Returns a boolean indicating whether or not the next token matches
     * the expected String value.  This does not remove the token from the
     * token list.
     *
     * @param expected value of the next token
     * @return true iff the token value matches the expected string
     */
    private boolean peekNext(String expected) {
        return tokens.get(0).getValue().equals(expected);
    }

    /**
     * Returns a boolean indicating whether or not the token directly after
     * the front most token matches the expected String value.  This does
     * not remove any tokens from the token list.
     *
     * @param expected value of the token directly after the next token
     * @return true iff the value matches the expected string
     */
    private boolean peekTwoAhead(String expected) {
        return tokens.get(1).getValue().equals(expected);
    }


    /*
     *  <program> ::= program <decls> begin <stmts> end ;
     */
    public ProgramAST parse() throws ParseException {

        try {
            // Needed for 'program' token position info
            Token startTok = tokens.get(0);

            checkNext("program");

            List<Declaration> decls = parseDecls();

            checkNext("begin");

            List<Statement> stmts = parseStmts();

            checkNext("end");
            checkNext(";");

            return new ProgramAST(decls, stmts, startTok);

            // This might happen if we do a tokens.get(), and nothing is there!
        } catch (IndexOutOfBoundsException ex) {

            throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
        }
    }

    /*
     *  <decls> ::= (  <decl>  )*
     */
    private List<Declaration> parseDecls() throws ParseException {

        List<Declaration> decls = new ArrayList<>();

        while (!peekNext("begin")) {
            Declaration decl = parseDecl();
            decls.add(decl);
        }

        return decls;
    }

    /*
     * <decl> ::= <var-decl> | <func-decl>
     */
    private Declaration parseDecl() throws ParseException {
        //System.out.println(tokens.get(0).getValue());
        if (peekTwoAhead(":")) {
            return parseVarDecl();
        } else if (peekTwoAhead("(")) {
            return parseFuncDecl();
        } else {
            Token tok = tokens.get(0);
            throw new ParseException("Declaration Error in line "+tok.getLine()+" and column "+tok.getColumn()+" with token "+tok.getValue(), tok);
        }
    }

    /*
     * <func-decl> ::= <label> ( <params> ) : <ret-type> is
     * 						<loc-var-decls> begin <stmts> end ;
     */
    private FunctionDecl parseFuncDecl() throws ParseException {
        // TODO Auto-generated method stub
        Token tok = tokens.remove(0);
        checkNext("(");
        List<VariableDecl> params = new ArrayList<>();
        while (!peekNext(")")) {
            params.add(parseVarDecl());
            if (peekNext(",")) {
                checkNext(",");
            }
        }
        checkNext(")");
        checkNext(":");
        Token returnType = tokens.remove(0);
        Type type = new Type(returnType);
        checkNext("is");
        List<VariableDecl> localVars = new ArrayList<>();
        while(peekTwoAhead(":")) {
            localVars.add(parseVarDecl());
        }
        checkNext("begin");
        List<Statement> stmts = parseStmts();
        checkNext("end");
        checkNext(";");
        return new FunctionDecl(tok, tok, params, type, localVars,stmts);
    }

    /*
     * <var-decl> ::= <label> : <type> ;
     */
    private VariableDecl parseVarDecl() throws ParseException {
        // TODO Auto-generated method stub
        Token tok = tokens.remove(0);
        String[] illegalVariableNames = {"while", "if", "else", "begin", "end", "do", "program",
                "then", "print", "return", "print_line", "and", "or", "not", "true","false",
                "void", "Integer", "Boolean", "String", "is"};
        for (String illegalVariableName : illegalVariableNames) {
            if (tok.getValue().equals(illegalVariableName)) {
                throw new ParseException("Invalid Variable Name: " + tok.getValue(), tok);
            }
        }

        checkNext(":");
        Token vType = tokens.remove(0);
        Type type = new Type(vType);
        if (peekNext(";")) {
            checkNext(";");
        }
        return new VariableDecl(tok, tok, type);
    }

    /*
     * <stmts> ::= (  <stmt>  )*
     */
    private List<Statement> parseStmts() throws ParseException {
        // TODO Auto-generated method stub
        List<Statement> stmts = new ArrayList<>();
        while (!(peekNext("end") ||peekNext("else"))) {
            Statement stmt = parseStmt();
            stmts.add(stmt);
        }
        return stmts;
    }

    /*
    * <stmts> ::= <ass> | <while> | <if> | <print> | <print_line> | <return>
    */
    private Statement parseStmt() throws ParseException {
        if (peekTwoAhead(":=")){
            return parseAssignment();
        } else if (peekNext("while")){
            return parseWhileDoLoop();
        } else if (peekNext("if")) {
            return parseIfStmts();
        } else if (peekNext("print")) {
            return parsePrint();
        } else if (peekNext("print_line")) {
            return parsePrintLine();
        } else if (peekNext("return")) {
            return parseReturn();
        } else if (peekTwoAhead("(")) {
            return parseFuncCall();
        } else throw new ParseException("Parsing Error caused by '"+tokens.get(0).getValue()+"'", tokens.get(0));

    }

    // parsing for statement elements



    // <ass> ::= <label> := <expr>;
    private Assignment parseAssignment() throws ParseException {
        Token tok = tokens.remove(0);
        checkNext(":=");
        Expression expr =parseExpression();
        checkNext(";");
        return new Assignment(tok, expr);
    }

    // <while> :: while <expr> do <stmts> end while ;
    private WhileDoLoop parseWhileDoLoop() throws ParseException {
        Token tok = tokens.remove(0);
        Expression expr = parseExpression();
        checkNext("do");
        List<Statement> statements = parseStmts();
        checkNext("end");
        checkNext("while");
        checkNext(";");
        return new WhileDoLoop(tok, expr, statements);
    }

    // <if> :: if <expr> then <stmts> end if ;
    // <if> :: if <expr> then <stmts> else <stmts> end if ;
    private IfStmts parseIfStmts() throws ParseException {
        Token tok = tokens.remove(0);
        Expression exp = parseExpression();
        checkNext("then");
        List<Statement> stmts = parseStmts();
        List<Statement> elseStmts = new ArrayList<>();;
        if (peekNext("else")) {
            checkNext("else");
            elseStmts = parseStmts();
            checkNext("end");
            checkNext("if");
            checkNext(";");
            return new IfStmts(tok, exp, stmts, elseStmts );
        }
        checkNext("end");
        checkNext("if");
        checkNext(";");
        return new IfStmts(tok, exp, stmts, elseStmts );
    }

    // <print> ::= print <expr> ;
    private Print parsePrint() throws ParseException {
        Token tok = tokens.remove(0);
        Expression expr = parseExpression();
        checkNext(";");
        return new Print(tok, expr);
    }

    // <print_line> ::= print_line ;
    private PrintLine parsePrintLine() throws ParseException {
        Token tok = tokens.remove(0);
        checkNext(";");
        return new PrintLine(tok);
    }

    // <return> ::= return <expr> ;
    // <return> ::= return;
    private Return parseReturn() throws ParseException {
        Token tok = tokens.remove(0);
        if (!peekNext(";")) {
            Expression expr = parseExpression();
            checkNext(";");
            return new Return(tok, expr);
        }
        checkNext(";");
        return new Return(tok);
    }

    // <funcCall> ::= <label> ( <args> ) ;
    private FuncCall parseFuncCall() throws ParseException {
        Token tok = tokens.remove(0);
        checkNext("(");
        List<Expression> args = new ArrayList<>();
        if(!peekNext(")")){
            if(peekTwoAhead(",")){
                while(peekTwoAhead(",")){
                    args.add(parseExpression());
                    checkNext(",");
                }
            }
            args.add(parseExpression());
            checkNext(")");
            checkNext(";");
            return new FuncCall(tok, args);
        }
        checkNext(")");
        checkNext(";");
        return new FuncCall(tok, args);
    }

    // <expr> ::= <var> | <bin> | <un> | <func-decl>
    private Expression parseExpression() throws ParseException {
        if(peekNext("(")) {
            if (peekTwoAhead("-") || peekTwoAhead("not")){
                return parseUnaryOp();
            } else return parseBinOp();
        } else if (peekTwoAhead("(")) {
            return parseFuncCallExp();
        } else return parseVar();
    }

    // <un> ::= ( <unary-op> <expr> )
    private UnaryOp parseUnaryOp() throws ParseException {
        Token tok = tokens.remove(0);
        Token operator = tokens.get(0);
        if (peekNext("-")) {
            checkNext("-");
        } else if (peekNext("not")) {
            checkNext("not");
        } else throw new ParseException(" Parser Error: Unary Operator was Expected at '"+tok.getValue()+"'", tok);
        Expression expression = parseExpression();
        checkNext(")");
        return new UnaryOp(tok, operator ,expression);
    }


    // <bin> ::= ( <expr> <bin-op> <expr> )
    private BinOp parseBinOp() throws ParseException {
        Token tok = tokens.remove(0);
        Expression leftExpr = parseExpression();
        Token operator = tokens.get(0);
        String[] binOp = {"and", "or", ">", "<", "==",">=", "<=","+","-","*","/", "%"};
        for (int i=0; i<12; i++) {
            if (peekNext(binOp[i])){
                checkNext(binOp[i]);
                break;
            } else if (i==11) {
                throw new ParseException(" Parsing Error: Binary Operator Expected ",tok);
            }
        }
        Expression rightExpr = parseExpression();
        checkNext(")");
        return new BinOp(tok, leftExpr, operator, rightExpr);
    }

    // <var> ::= <label>
    private Variable parseVar() {
        Token tok = tokens.remove(0);
        return new Variable(tok);
    }

    // <funcCall> ::= <label> ( <args> ) ;
    private FuncCallExp parseFuncCallExp() throws ParseException{
        Token tok = tokens.remove(0);
        checkNext("(");
        List<Expression> args = new ArrayList<>();
        if(!peekNext(")")){
            if(peekTwoAhead(",")){
                while(peekTwoAhead(",")){
                    args.add(parseExpression());
                    checkNext(",");
                }
            }
            args.add(parseExpression());
            checkNext(")");
            return new FuncCallExp(tok, args);
        }
        checkNext(")");
        return new FuncCallExp(tok);
    }


}
