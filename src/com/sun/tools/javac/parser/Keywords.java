/**
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Use and Distribution is subject to the Java Research License available
 * at <http://wwws.sun.com/software/communitysource/jrl.html>.
 */

package com.sun.tools.javac.parser;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Version;

import static com.sun.tools.javac.parser.Token.*;

/**
 * Map from Name to Token and Token to String.
 * 
 * <p><b>This is NOT part of any API supported by Sun Microsystems.
 * If you write code that depends on this, you do so at your own risk.
 * This code and its internal interfaces are subject to change or
 * deletion without notice.</b>
 */
@Version("%W% %E%")
public class Keywords {
    public static final Context.Key<Keywords> keywordsKey =
	new Context.Key<Keywords>();

    public static Keywords instance(Context context) {
	Keywords instance = context.get(keywordsKey);
	if (instance == null)
	    instance = new Keywords(context);
	return instance;
    }

    private final Log log;
    private final Name.Table names;

    protected Keywords(Context context) {
	context.put(keywordsKey, this);
	log = Log.instance(context);
	names = Name.Table.instance(context);

	for (Token t : Token.values()) {
	    if (t.name != null)
		enterKeyword(t.name, t);
	    else
		tokenName[t.ordinal()] = null;
	}

	key = new Token[maxKey+1];
	for (int i = 0; i <= maxKey; i++) key[i] = IDENTIFIER;
	for (Token t : Token.values()) {
	    if (t.name != null)
		key[tokenName[t.ordinal()].index] = t;
	}
    }


    public Token key(Name name) {
	return (name.index > maxKey) ? IDENTIFIER : key[name.index];
    }

    /**
     * Keyword array. Maps name indices to Token.
     */
    private final Token[] key;

    /**	 The number of the last entered keyword.
     */
    private int maxKey = 0;

    /** The names of all tokens.
     */
    private Name[] tokenName = new Name[Token.values().length];

    public String token2string(Token token) {
	switch (token) {
	case IDENTIFIER:
	    return log.getLocalizedString("token.identifier");
	case CHARLITERAL:
	    return log.getLocalizedString("token.character");
	case STRINGLITERAL:
	    return log.getLocalizedString("token.string");
	case INTLITERAL:
	    return log.getLocalizedString("token.integer");
	case LONGLITERAL:
	    return log.getLocalizedString("token.long-integer");
	case FLOATLITERAL:
	    return log.getLocalizedString("token.float");
	case DOUBLELITERAL:
	    return log.getLocalizedString("token.double");
	case ERROR:
	    return log.getLocalizedString("token.bad-symbol");
	case EOF:
	    return log.getLocalizedString("token.end-of-input");
	case DOT: case COMMA: case SEMI: case LPAREN: case RPAREN:
	case LBRACKET: case RBRACKET: case LBRACE: case RBRACE:
	    return "'" + token.name + "'";
	default:
	    return token.name;
	}
    }

    private void enterKeyword(String s, Token token) {
	Name n = names.fromString(s);
	tokenName[token.ordinal()] = n;
	if (n.index > maxKey) maxKey = n.index;
    }
}
