package splat.parser.elements;

import splat.lexer.Token;
import splat.semanticanalyzer.SemanticAnalysisException;
import java.util.Map;
public class PrintLine extends Statement {
    public PrintLine( Token tok) {

        super(tok);
    }
    @Override
    public void analyze(Map<String, FunctionDecl> funcMap,
                        Map<String, Type> varAndParamMap) throws SemanticAnalysisException {}
}
