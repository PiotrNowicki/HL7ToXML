package com.piotrnowicki.hl7toxml.model;

/**
 * Component within Field. Can have subcomponents.
 * 
 * @author Piotr Nowicki
 * 
 */
public class Component {

	/**
	 * ID of component (taken from HL 2.x specification). Doesn't have to be
	 * unique, because repetitions of fields are possible.
	 */
	private Integer id;

	/**
	 * Name under which component will be represented in result XML.
	 */
	private String name;

	private String value;

	private boolean isAttribute;

	private SubComponent[] subcomponents;

	public boolean isSingleValue() {
		if (subcomponents.length == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append("Component ID: " + id + ", value: " + value);

		if (!isSingleValue()) {
			for (int i = 0; i < subcomponents.length; i++) {
				result.append("\r\n--------- Subcomponent [" + subcomponents[i]
						+ "]");
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

	public SubComponent[] getSubcomponents() {
		return subcomponents;
	}

	public void setSubcomponents(SubComponent[] subcomponents) {
		this.subcomponents = subcomponents;
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
