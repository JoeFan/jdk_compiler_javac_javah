/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL.  Use is subject to license terms.
 */

package com.sun.tools.javadoc;


import com.sun.javadoc.*;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Pair;


/**
 * Represents an annotation.
 * An annotation associates a value with each element of an annotation type.
 * Sure it ought to be called "Annotation", but that clashes with
 * java.lang.annotation.Annotation.
 * 
 * @author Scott Seligman
 * @version %I% %E%
 * @since 1.5
 */

public class AnnotationDescImpl implements AnnotationDesc {

    private final DocEnv env;
    private final Attribute.Compound annotation;


    AnnotationDescImpl(DocEnv env, Attribute.Compound annotation) {
	this.env = env;
	this.annotation = annotation;
    }

    /**
     * Returns the annotation type of this annotation.
     */
    public AnnotationTypeDoc annotationType() {
	ClassSymbol atsym = (ClassSymbol)annotation.type.tsym;
	return (AnnotationTypeDoc)env.getClassDoc(atsym);
    }

    /**
     * Returns this annotation's elements and their values.
     * Only those explicitly present in the annotation are
     * included, not those assuming their default values.
     * Returns an empty array if there are none.
     */
    public ElementValuePair[] elementValues() {
	List<Pair<MethodSymbol,Attribute>> vals = annotation.values;
	ElementValuePair res[] = new ElementValuePair[vals.length()];
	int i = 0;
	for (Pair<MethodSymbol,Attribute> val : vals) {
	    res[i++] = new ElementValuePairImpl(env, val.fst, val.snd);
	}
	return res;
    }

    /**
     * Returns a string representation of this annotation.
     * String is of one of the forms:
     *     @com.example.foo(name1=val1, name2=val2)
     *     @com.example.foo(val)
     *     @com.example.foo
     * Omit parens for marker annotations, and omit "value=" when allowed.
     */
    public String toString() {
	StringBuffer sb = new StringBuffer("@");
	sb.append(annotation.type.tsym);

	ElementValuePair vals[] = elementValues();
	if (vals.length > 0) {		// omit parens for marker annotation
	    sb.append('(');
	    boolean first = true;
	    for (ElementValuePair val : vals) {
		if (!first) {
		    sb.append(", ");
		}
		first = false;

		String name = val.element().name();
		if (vals.length == 1 && name.equals("value")) {
		    sb.append(val.value());
		} else {
		    sb.append(val);
		}
	    }
	    sb.append(')');
	}
	return sb.toString();
    }


    /**
     * Represents an association between an annotation type element
     * and one of its values.
     */
    public static class ElementValuePairImpl implements ElementValuePair {

	private final DocEnv env;
	private final MethodSymbol meth;
	private final Attribute value;

	ElementValuePairImpl(DocEnv env, MethodSymbol meth, Attribute value) {
	    this.env = env;
	    this.meth = meth;
	    this.value = value;
	}

	/**
	 * Returns the annotation type element.
	 */
	public AnnotationTypeElementDoc element() {
	    return env.getAnnotationTypeElementDoc(meth);
	}

	/**
	 * Returns the value associated with the annotation type element.
	 */
	public AnnotationValue value() {
	    return new AnnotationValueImpl(env, value);
	}

	/**
	 * Returns a string representation of this pair
	 * of the form "name=value".
	 */
	public String toString() {
	    return meth.name + "=" + value();
	}
    }
}
