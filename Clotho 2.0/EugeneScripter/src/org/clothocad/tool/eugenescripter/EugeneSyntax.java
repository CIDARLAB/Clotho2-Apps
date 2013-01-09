/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.eugenescripter;

/**
 *
 * @author cassie
 */
import eugene.parser.EugeneLexer;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.List;
import javax.swing.text.Segment;
import jsyntaxpane.Lexer;
import jsyntaxpane.Token;
import jsyntaxpane.TokenType;
import org.antlr.runtime.ANTLRReaderStream;

public class EugeneSyntax implements Lexer {
    //translate antlr tokens to jsyntaxpane tokens
    //here, have a lookup table

    @Override
    public void parse(Segment sgmnt, int i, List<jsyntaxpane.Token> list) {
        try {
            CharArrayReader reader = new CharArrayReader(sgmnt.array, sgmnt.offset, sgmnt.count);
            eugene.parser.EugeneLexer lexer = new EugeneLexer(new ANTLRReaderStream(reader));

            while (lexer.getCharIndex() < sgmnt.count) {
                //as long as there's something else to lex, lex and turn into token
                int start = lexer.getCharIndex();
                int nextTokenType = lexer.nextToken().getType();
                int length = lexer.getCharIndex() - start;
                switch (nextTokenType) {
                    case 4:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 5:
                        list.add(new Token(TokenType.COMMENT, start, length));
                        break;
                    case 6:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 7:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 8:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 9:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 10:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 11:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 12:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 13:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 14:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 15:
                        list.add(new Token(TokenType.STRING, start, length));
                        break;
                    case 16:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 17:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 18:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 19:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 20:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 21:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 22:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 23:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 24:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 25:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 26:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 27:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 28:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 29:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 30:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 31:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 32:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 33:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 34:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 35:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 36:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 37:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 38:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 39:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 40:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 41:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 42:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 43:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 44:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 45:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 46:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 47:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 48:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 49:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 50:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 51:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 52:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 53:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 54:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 55:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 56:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 57:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 58:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 59:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 60:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 61:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 62:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 63:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 64:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 65:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 66:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 67:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 68:
                        list.add(new Token(TokenType.OPERATOR, start, length));
                        break;
                    case 69:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 70:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 71:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 72:
                        list.add(new Token(TokenType.TYPE, start, length));
                        break;
                    case 73:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 74:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 75:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 76:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 77:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 78:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 79:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 80:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 81:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 82:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 83:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 84:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 85:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 86:
                        list.add(new Token(TokenType.TYPE, start, length));
                        break;
                    case 87:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 88:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 89:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 90:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 91:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 92:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 93:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 94:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 95:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 96:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 97:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 98:
                        list.add(new Token(TokenType.TYPE, start, length));
                        break;
                    case 99:
                        list.add(new Token(TokenType.KEYWORD, start, length));
                        break;
                    case 100:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 101:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    case 102:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                    default:
                        list.add(new Token(TokenType.DEFAULT, start, length));
                        break;
                }
            }
        } catch (IOException ex) {
        }
    }
}
