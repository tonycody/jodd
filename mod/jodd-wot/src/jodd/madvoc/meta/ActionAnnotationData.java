// Copyright (c) 2003-2011, Jodd Team (jodd.org). All Rights Reserved.

package jodd.madvoc.meta;

import jodd.util.AnnotationDataReader;

import java.lang.annotation.Annotation;

/**
 * Holds action annotation values.
 */
public class ActionAnnotationData<A extends Annotation> extends AnnotationDataReader.AnnotationData<A> {

	protected String value;
	protected String extension;
	protected String alias;
	protected String method;

	protected ActionAnnotationData(A annotation) {
		super(annotation);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}