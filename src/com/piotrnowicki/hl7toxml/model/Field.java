package com.piotrnowicki.hl7toxml.model;

/**
 * Single field (with subfields if occurs).
 * 
 * @author Piotr Nowicki
 * 
 */
public class Field {

	/**
	 * ID of field (taken from HL 2.x specification). Doesn't have to be unique,
	 * because repetitions of fields are possible.
	 */
	private Integer id;

	/**
	 * Name under which field will be represented in result XML.
	 */
	private String name;

	private String value;

	private boolean isAttribute;

	private Component[] components;

	public Field() {
	}

	public Field(Integer id, String name, String value, Component[] subfields) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.components = subfields;
	}

	public boolean isSingleValue() {
		if (components.length == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append("Field ID: " + id + ", value: " + value);

		if (!isSingleValue()) {
			for (int i = 0; i < components.length; i++) {
				result.append("\r\n------ [" + components[i] + "]");
			}
		}

		return result.toString();
	}

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

	public Component[] getComponents() {
		return components;
	}

	public void setComponents(Component[] subfields) {
		this.components = subfields;
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
