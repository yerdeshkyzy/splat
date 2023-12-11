package splat.parser.elements;

import java.util.Map;
import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;

public class Type extends ASTElement {
    Token tok;

    public Type (Token tok) {
        super(tok);
        this.tok = tok;
    }

    @Override
    public  String toString() {
        return tok.getValue();
    }
}
