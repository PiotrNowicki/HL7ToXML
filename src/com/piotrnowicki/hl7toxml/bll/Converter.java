package com.piotrnowicki.hl7toxml.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.piotrnowicki.hl7toxml.bll.config.MessageConfig;
import com.piotrnowicki.hl7toxml.model.Component;
import com.piotrnowicki.hl7toxml.model.Field;
import com.piotrnowicki.hl7toxml.model.Segment;
import com.piotrnowicki.hl7toxml.model.SubComponent;

/**
 * Converts given string input from HL7 Message to form Java Segment object.
 * 
 * @author Piotr Nowicki
 * 
 */
public class Converter {

	private ConfigHelper config = ConfigHelper.getInstance();

	/**
	 * Parse segment given as a String and return proper Java Object.
	 * 
	 * @param input
	 * @return
	 */
	public Segment parse(String input) {
		Segment result = new Segment();

		String[] sect = input.split(MessageConfig.FIELD_SEPARATOR);

		String segmentCode = config
				.getName(config.getSegmentXPathConf(sect[0]));

		result.setCode((segmentCode != null) ? segmentCode : sect[0]);
		result.setFields(parseFields(sect, config.getSegmentXPathConf(sect[0])));

		return result;
	}

	/**
	 * Create Fields objects from Strings basing on configuration.
	 * 
	 * @param inputFields
	 * @param segmentXPathConf
	 * @return
	 */
	private Map<Integer, List<Field>> parseFields(String[] inputFields,
			String segmentXPathConf) {
		Map<Integer, List<Field>> result = new TreeMap<Integer, List<Field>>();

		for (int i = 1; i < inputFields.length; i++) {
			List<Field> fields = new ArrayList<Field>();

			String[] fieldsRepeated = inputFields[i]
					.split(MessageConfig.REPEAT_SEPARATOR);

			for (int k = 0; k < fieldsRepeated.length; k++) {
				String fieldXPath = config.getFieldXPathConf(segmentXPathConf,
						i + "");

				Component[] components = parseComponents(
						fieldsRepeated[k]
								.split(MessageConfig.COMPONENT_SEPARATOR),
						fieldXPath);

				Field field = new Field();

				field.setComponents((components.length > 1) ? components : null);
				field.setId(i);
				field.setName(config.getName(fieldXPath));
				field.setValue(fieldsRepeated[k]);
				field.setAttribute(config.isAttribute(fieldXPath));

				fields.add(field);
			}

			result.put(i, fields);
		}

		return result;
	}

	/**
	 * Create Components objects from Strings basing on configuration.
	 * 
	 * @param components
	 * @param fieldXPathConf
	 * 
	 * @return
	 */
	private Component[] parseComponents(String[] components,
			String fieldXPathConf) {
		List<Component> result = new ArrayList<Component>();

		for (int i = 0; i < components.length; i++) {
			String componentXPath = config.getComponentXPathConf(
					fieldXPathConf, (i + 1) + "");

			SubComponent[] subcomponents = parseSubComponents(
					components[i].split(MessageConfig.SUBCOMPONENT_SEPARATOR),
					componentXPath);

			Component component = new Component();
			component.setId(i + 1);
			component.setName(config.getName(componentXPath));
			component
					.setSubcomponents((subcomponents.length > 1) ? subcomponents
							: null);
			component.setValue(components[i]);
			component.setAttribute(config.isAttribute(componentXPath));

			result.add(component);
		}

		return result.toArray(new Component[result.size()]);
	}

	/**
	 * Create SubComponents objects from Strings basing on configuration.
	 * 
	 * @param subcomponents
	 * @param componentXPathConf
	 * 
	 * @return
	 */
	private SubComponent[] parseSubComponents(String[] subcomponents,
			String componentXPathConf) {
		List<SubComponent> result = new ArrayList<SubComponent>();

		for (int i = 0; i < subcomponents.length; i++) {
			String sCompXPath = config.getSubComponentXPathConf(
					componentXPathConf, (i + 1) + "");

			SubComponent subcomponent = new SubComponent();
			subcomponent.setId(i + 1);
			subcomponent.setName(config.getName(sCompXPath));
			subcomponent.setValue(subcomponents[i]);
			subcomponent.setAttribute(config.isAttribute(sCompXPath));

			result.add(subcomponent);
		}

		return result.toArray(new SubComponent[result.size()]);
	}
}
