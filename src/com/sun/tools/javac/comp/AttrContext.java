/**
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Use and Distribution is subject to the Java Research License available
 * at <http://wwws.sun.com/software/communitysource/jrl.html>.
 */

package com.sun.tools.javac.comp;

import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;

/** Contains information specific to the attribute and enter
 *  passes, to be used in place of the generic field in environments.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
@Version("%W% %E%")
public class AttrContext {

    /** The scope of local symbols.
     */
    Scope scope = null;

    /** The number of enclosing `static' modifiers.
     */
    int staticLevel = 0;

    /** Is this an environment for a this(...) or super(...) call?
     */
    boolean isSelfCall = false;

    /** Are we evaluating the selector of a `super' or type name?
     */
    boolean selectSuper = false;

    /** Are arguments to current function applications boxed into an array for varargs?
     */
    boolean varArgs = false;

    /** A list of type variables that are all-quantifed in current context.
     */
    List<Type> tvars = List.nil();

    /** A record of the lint/SuppressWarnings currently in effect
     */
    Lint lint;

    /** Duplicate this context, replacing scope field and copying all others.
     */
    AttrContext dup(Scope scope) {
	AttrContext info = new AttrContext();
	info.scope = scope;
	info.staticLevel = staticLevel;
	info.isSelfCall = isSelfCall;
	info.selectSuper = selectSuper;
	info.varArgs = varArgs;
	info.tvars = tvars;
	info.lint = lint;
	return info;
    }

    /** Duplicate this context, copying all fields.
     */
    AttrContext dup() {
	return dup(scope);
    }
    
    public Iterable<Symbol> getLocalElements() {
        if (scope == null)
            return List.nil();
        return scope.getElements();
    }
    
    public String toString() {
        return "AttrContext[" + scope.toString() + "]";
    }
}

