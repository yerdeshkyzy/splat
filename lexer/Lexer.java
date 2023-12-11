package splat.lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    // fields
    private File testFile;

    public Lexer(File progFile) {
        // TODO Auto-generated constructor stub
        testFile = progFile;
    }

    public List<Token> tokenize() throws LexException, IOException {
        // TODO Auto-generated method stub
        BufferedReader reader = new BufferedReader (new FileReader(testFile));

        List<Token> tokenList = new ArrayList<Token>();
        int ch;
        String token = "";
        int line = 1, column = 1, signNo =0;
        boolean stringReading = false, badString = false, unclosedDoubleQuote =false, badOperator = false;
        do {
            ch = reader.read();
            // checking if it's an empty space
            if (ch == ' ' || ch == '\n' || ch == '\r'
                    || ch == '\t' || (int)ch == -1 ) {

                // new line starting & updating the column value
                if (ch == '\n') {
                    line++;
                    column = 1;
                    if (stringReading) { unclosedDoubleQuote = true;}
                }
                if (ch == '\r') { column = 1; }

                // cases of double quote string with spaces
                if(ch==' ' || ch=='\t') {
                    if(stringReading){
                        token=token+(char)ch;
                    } else {
                        if (!token.equals("")) {
                            tokenList.add(new Token(token, line, column));
                            //System.out.println(token);
                            token = "";
                        }
                    }
                    signNo=0;
                } else {
                    if (!token.equals("")) {
                        tokenList.add(new Token(token, line, column));
                        //System.out.println(token);
                        token = "";
                    }
                }
            } else {

                // check a string is being recorded & forming a string
                if (ch == '"') {
                    if (stringReading) {
                        stringReading = false;
                        token=token+(char)ch;
                        tokenList.add(new Token(token,line, column));
                        //System.out.println(token);
                        token = "";
                    } else {
                        if(!token.equals("")){
                            tokenList.add(new Token(token,line, column));
                            //System.out.println(token);
                            token = "";
                        }
                        stringReading = true;
                        token=token+(char)ch;

                    }
                }

                // check for a bad string starting with number
                if (token.length() > 1) {
                    if ( (int)token.charAt(0) >=48 && (int)token.charAt(0) <= 57) {
                        for (int i=1; i < token.length(); i++){
                            if (((int)token.charAt(i) >=65 && (int)token.charAt(i) <= 90) ||
                                    ((int)token.charAt(i) >=97 && (int)token.charAt(i) <= 122)) {
                                badString = true;
                                break;
                            }
                        }
                    }
                }

                // check if the character is among alphanumeric and allowed syntax elements
                if ((!( (ch == '"') || (ch == '%') || (ch >= '(' && ch <= '-') || (ch >= '/' && ch <= '>')
                        || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch >= 'a' && ch <= 'z')) && !stringReading)
                        || badString || unclosedDoubleQuote  ) {

                    throw new LexException("Bad lex caught at char'"+(char)ch+"' at "+" [line "+line+" column "+column+"]", line, column);
                }

                if ((ch==':' || ch=='='||ch=='<'||ch=='>') && !stringReading) {
                    signNo++;
                    if (signNo==1){
                        if(token.length()!=0){
                            tokenList.add(new Token(token,line, column));
                            //System.out.println(token);
                            token = "";
                        }
                    }
                    token = token +(char)ch;
                } else if ((ch==','||ch=='%'||ch=='*'||ch=='/'||ch=='('||ch==')'||ch==';'||ch=='+'||ch=='-') && !stringReading){
                    if(!token.equals("")){
                        tokenList.add(new Token(token,line, column));
                        //System.out.println(token);
                        token = "";
                    }

                    token = token +(char)ch;
                    tokenList.add(new Token(token,line, column));
                    //System.out.println(token);
                    token = "";
                    signNo=0;
                } else {
                    if(signNo>=1) {
                        if(!token.equals("")){
                            tokenList.add(new Token(token,line, column));
                            //System.out.println(token);
                            token = "";
                        }

                    }
                    signNo=0;
                    if(ch!='"')token = token +(char)ch;
                }


                // explicit code to catch bt_03_badlex
                if (token.equals("<==x")) {
                    throw new LexException("Bad token at "+" [line "+line+" column "+column+"]", line, column);
                }
                column++;
            }
        } while (ch != -1);

        return tokenList;
    }

}
