package com.piotrnowicki.hl7toxml.bll;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.regexp.RE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.piotrnowicki.hl7toxml.bll.config.MessageConfig;
import com.piotrnowicki.hl7toxml.bll.config.SystemConfig;
import com.piotrnowicki.hl7toxml.model.Component;
import com.piotrnowicki.hl7toxml.model.Field;
import com.piotrnowicki.hl7toxml.model.Segment;
import com.piotrnowicki.hl7toxml.model.SubComponent;

/**
 * Helper class for creating XML file documents from DOM.
 * 
 * @author Piotr Nowicki
 * 
 */
public class XMLHelper {
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document dom;

	public XMLHelper() throws ParserConfigurationException {
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
	}

	/**
	 * Create XML document.
	 */
	public void createDocument(String filename, List<Segment> segments) {
		dom = db.newDocument();

		dom.setDocumentURI(SystemConfig.XML_NS);
		Element rootEl = dom.createElement(SystemConfig.XML_ROOT_NAME);
		dom.appendChild(rootEl);

		for (Segment segment : segments) {
			Element segmentEl = dom.createElement(segment.getCode());

			addFields(segment.getFields(), segmentEl);

			rootEl.appendChild(segmentEl);
		}

		printToFile(dom, filename);
	}

	/**
	 * Write created XML structure into the file.
	 * 
	 * @param dom
	 * @param filename
	 */
	public void printToFile(Document dom, String filename) {

		try {
			DOMSource source = new DOMSource(dom);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			StreamResult result = new StreamResult(new File(
					SystemConfig.XML_OUTPUT + filename));
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse segments from given HL7 2.x file.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<Segment> parseMessage(String filename) throws IOException {
		List<Segment> result = new ArrayList<Segment>();

		Converter sConverter = new Converter();

		File file = new File(filename);
		List<String> lines = FileUtils.readLines(file, "UTF-8");
		lines = hl7Trim(lines);

		for (String str : lines) {
			if (str.startsWith("MSH")) {
				MessageConfig.initConfig(str);
				// If it's a Message Header, we need to do this trick in order
				// to prevail field order (there are encoding characters in MSH
				// which are crushing overall mechanism)
				str = "MSH" + "||" + str.subSequence(8, str.length());

				// We need to parse the first line twice, to get known the
				// message type BEFORE the real parsing
				Segment tempSegment = sConverter.parse(str);
				MessageConfig.setMessageXpath(tempSegment.getFields().get(9)
						.get(0));

			}

			Segment s = sConverter.parse(str);
			result.add(s);
		}

		return result;
	}

	/**
	 * Create Fields structure and add it to the given element (segment).
	 */
	private void addFields(Map<Integer, List<Field>> fields, Element segment) {

		// Some fields can have repetitions - in this case they are signed as
		// the same field name.
		for (Entry<Integer, List<Field>> fieldsSect : fields.entrySet()) {

			// For each field (in repetitions)
			for (Field field : fieldsSect.getValue()) {

				// We are omitting not-defined values, so if no name is
				// specified - proceed to next element.
				if (shouldOmitElement(field.getName())) {
					continue;
				}

				// Add field element if it's not empty or does contain
				// components
				if ((field.getValue() != null && !field.getValue().equals(""))
						|| field.getComponents() != null) {

					// We have components, so we parse them
					if (field.getComponents() != null) {
						Element fieldEl = dom.createElement(getTagName(
								field.getName(),
								SystemConfig.DEFAULT_FIELD_PREFIX
										+ field.getId()));

						addComponents(field.getComponents(), fieldEl);

						segment.appendChild(fieldEl);
					}

					// No components to parse - so don't do it (avoid empty
					// nodes)
					else {
						String fieldId = getTagName(
								field.getName(),
								SystemConfig.DEFAULT_FIELD_PREFIX
										+ field.getId());

						if (field.isAttribute()) {
							segment.setAttribute(fieldId, field.getValue());
						} else {
							Element fieldEl = createTextElement(dom, fieldId,
									field.getValue());
							segment.appendChild(fieldEl);

						}

					}

				}
			}
		}

	}

	/**
	 * Create Components structure and add it to the given element (field).
	 */
	private void addComponents(Component[] components, Element field) {

		// Add element only if it contains more than 1 element - we don't want
		// to make a mess in XML
		if (components != null) {
			for (Component comp : components) {

				String elName = getTagName(comp.getName(),
						SystemConfig.DEFAULT_COMPONENT_PREFIX + comp.getId());

				Element componentEl = dom.createElement(elName);

				// We are ommiting not-defined values, so if no name is
				// specified - proceeed to next element.
				if (shouldOmitElement(comp.getName())) {
					continue;
				}

				// We have subcomponents, so we parse them
				if (comp.getSubcomponents() != null) {
					addSubcomponents(comp.getSubcomponents(), componentEl);
					field.appendChild(componentEl);
				}

				// No subcomponents to parse - so don't do it (avoid empty
				// nodes)
				else if (comp.getValue() != null && !comp.getValue().equals("")) {

					if (comp.isAttribute()) {
						field.setAttribute(elName, comp.getValue());
					} else {
						componentEl.setTextContent(comp.getValue());
						field.appendChild(componentEl);
					}
				}

			}
		}
	}

	/**
	 * Create Subcomponents structure and add it to the given element
	 * (component).
	 */
	private void addSubcomponents(SubComponent[] subcomponents,
			Element component) {

		// Add element only if it contains more than 1 element - we don't want
		// to make a mess in XML
		if (subcomponents != null) {
			for (int i = 0; i < subcomponents.length; i++) {
				SubComponent s = subcomponents[i];

				// We are ommiting not-defined values, so if no name is
				// specified - proceeed to next element.
				if (shouldOmitElement(s.getName())) {
					continue;
				}

				String elName = getTagName(s.getName(),
						SystemConfig.DEFAULT_SUBCOMPONENT_PREFIX + (i + 1));

				if (s.isAttribute()) {
					component.setAttribute(elName, s.getValue());
				} else {
					Element subComponentEl = createTextElement(dom, elName,
							s.getValue());

					component.appendChild(subComponentEl);
				}
			}
		}
	}

	/**
	 * Remove unexpected HL7 chars from beginning of each line. They might occur
	 * iin OUT communicates and are not welcomed in result XML.
	 * 
	 * @param input
	 * @return
	 */
	private List<String> hl7Trim(List<String> inputLines) {
		List<String> result = new ArrayList<String>();

		// First character is not a segment name (3 upper-case letters) and
		// the line isn't empty. It means it contains 'garbage' we need to
		// get rid of.
		RE segmentName = new RE("^[^a-zA-Z]");

		for (String input : inputLines) {
			String trimmedLine = new String();

			if (segmentName.match(input)) {
				trimmedLine = input.substring(1, input.length());
			} else {
				trimmedLine = input;
			}

			// Do standard trim
			trimmedLine.trim();

			// If after those trimmings we end with empty line - do not parse
			// it.
			if (!trimmedLine.isEmpty()) {
				result.add(trimmedLine);
			}
		}

		return result;
	}

	/**
	 * Print result tag name.
	 * 
	 * @param nameValue
	 * @param noNameValue
	 * @return
	 */
	private String getTagName(String nameValue, String noNameValue) {
		if (nameValue == null) {
			return noNameValue;
		}

		return nameValue;
	}

	/**
	 * Create XML element with given name and value.
	 * 
	 * @param dom
	 * @return
	 */
	private Element createTextElement(Document dom, String name, String value) {

		Element nameEl = dom.createElement(name);
		nameEl.setTextContent(value);

		return nameEl;
	}

	/**
	 * Should we omit this element?
	 * 
	 * @param name
	 * @return
	 */
	private Boolean shouldOmitElement(String name) {
		// We are ommiting not-defined values, so if no name is specified -
		// proceeed to next element.
		if (name == null
				&& ConfigHelper.getInstance().getBoolSystemConfig(
						SystemConfig.XML_CONF_OPTION_OMIT_UNDEF)) {
			return true;
		}

		return false;
	}

}
