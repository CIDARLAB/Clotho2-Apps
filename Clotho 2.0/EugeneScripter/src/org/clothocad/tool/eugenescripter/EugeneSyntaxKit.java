package org.clothocad.tool.eugenescripter;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.Lexer;

public class EugeneSyntaxKit extends DefaultSyntaxKit {

    public EugeneSyntaxKit() {
        super(new EugeneSyntax());
    }

    EugeneSyntaxKit(Lexer lexer) {
        super(lexer);
    }
}
