package com.piotrnowicki.hl7toxml.model;

/**
 * Information about element - what should be it's name, should it be an
 * single-element or an attribute of it's parent, etc.
 * 
 * @author Piotr Nowicki
 * 
 */
public class ResultXMLElement {
	private String name;
	private boolean isAttribute;

	public ResultXMLElement() {
	}

	public ResultXMLElement(String name, boolean isAttribute) {
		this.name = name;
		this.isAttribute = isAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAttribute() {
		return isAttribute;
	}

	public void setAttribute(boolean isAttribute) {
		this.isAttribute = isAttribute;
	}

}
