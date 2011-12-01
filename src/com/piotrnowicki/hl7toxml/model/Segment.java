package com.piotrnowicki.hl7toxml.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Segment in HL7 Message.
 * 
 * @author Piotr Nowicki
 * 
 */
public class Segment {

	/**
	 * Three letter code of message segment
	 */
	private String code;

	/**
	 * Fields in segment.
	 * 
	 * Key is Number of field.
	 * 
	 * Value is a list of fields (list because repetitions are possible). If no
	 * fields occurs - the value is null (but the ID does extist)
	 * 
	 */
	private Map<Integer, List<Field>> fields;

	/**
	 * For printing for debug purposes only.
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Segment name: " + code);

		for (Entry<Integer, List<Field>> fds : fields.entrySet()) {
			for (Field f : fds.getValue()) {
				result.append("\r\n--- [" + f + "]");
			}
		}

		return result.toString();
	}

	/**
	 * Segment is equal to another if it's name is equal.
	 */
	@Override
	public boolean equals(Object obj) {
		Segment compareTo = (Segment) obj;

		if (compareTo.getCode().equals(code)) {
			return true;
		} else {
			return false;
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<Integer, List<Field>> getFields() {
		return fields;
	}

	public void setFields(Map<Integer, List<Field>> fields) {
		this.fields = fields;
	}
}
