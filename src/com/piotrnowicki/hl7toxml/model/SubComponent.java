package com.piotrnowicki.hl7toxml.model;

/**
 * Subcomponent within Component. Final structure.
 * 
 * @author Piotr Nowicki
 * 
 */
public class SubComponent {

	/**
	 * ID of subcomponent (taken from HL 2.x specification).
	 */
	private Integer id;

	/**
	 * Name under which component will be represented in result XML.
	 */
	private String name;

	private String value;

	private boolean isAttribute;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
