package com.piotrnowicki.hl7toxml.bll;

import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_ATTR_ID;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_ATTR_IS_ATTRIBUTE;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_ATTR_IS_DEFAULT;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_ATTR_NAME;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_ATTR_SUBID;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_CONF;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_CONF_OPTION;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_EL_COMPONENT;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_EL_FIELD;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_EL_MESSAGE;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_EL_SEGMENT;
import static com.piotrnowicki.hl7toxml.bll.config.SystemConfig.XML_EL_SUBCOMPONENT;

import java.util.NoSuchElementException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

import com.piotrnowicki.hl7toxml.bll.config.MessageConfig;
import com.piotrnowicki.hl7toxml.bll.config.SystemConfig;

/**
 * Helper for reading configuration file describing how the result XML should
 * look like.
 * 
 * @author Piotr Nowicki
 * 
 */
public class ConfigHelper {

	private static ConfigHelper instance;

	private XMLConfiguration config;

	public static ConfigHelper getInstance() {
		if (instance == null) {
			instance = new ConfigHelper();
		}

		return instance;
	}

	private ConfigHelper() {
		try {
			config = new XMLConfiguration(SystemConfig.CONFIG_FILE);
			config.setExpressionEngine(new XPathExpressionEngine());
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Get system-wide config value as a boolean type.
	 * 
	 * @param configName
	 *            name of configuration value.
	 * 
	 * @return true if option is set or false otherwise.
	 */
	public Boolean getBoolSystemConfig(String configName) {
		String xpath = XML_CONF + "/" + XML_CONF_OPTION + "[@id='" + configName
				+ "']/@value";

		Boolean result = config.getBoolean(xpath);

		return result;
	}

	/**
	 * Get message XPath Expression to read configuration.
	 * 
	 * @param messageId
	 * @param messageSubId
	 * 
	 * @return XPath expression to get to concrete message.
	 */
	public String getMessageXPathConf(String messageId, String messageSubId) {
		String xpath = XML_EL_MESSAGE + "[@" + XML_ATTR_ID + "='" + messageId
				+ "']";

		if (messageSubId != null) {
			xpath += "[@" + XML_ATTR_SUBID + "='" + messageSubId + "']";
		}

		return xpath;
	}

	/**
	 * Get segment XPath Expression to read configuration. Take message-specific
	 * segment definition. If it doesn't exist - take the default one (attribute
	 * 'isDefault=true'.
	 * 
	 * @param segmentId
	 * @return
	 */
	public String getSegmentXPathConf(String segmentId) {
		String xpath = MessageConfig.MESSAGE_XPATH + "/" + XML_EL_SEGMENT
				+ "[@" + XML_ATTR_ID + "='" + segmentId + "']";

		// Message-specific segment definition doesn't exist - take the default
		// one (if defined)
		if (config.getString(xpath) == null
				|| config.getString(xpath).equals("null")) {
			xpath = "//" + XML_EL_SEGMENT + "[@" + XML_ATTR_ID + "='"
					+ segmentId + "']" + "[@" + XML_ATTR_IS_DEFAULT
					+ "='true']";
		}

		return xpath;
	}

	/**
	 * Get field XPath Expression to read configuration.
	 * 
	 * @param fieldId
	 * @return
	 */
	public String getFieldXPathConf(String segmentXPath, String fieldId) {
		String xpath = segmentXPath + "/" + XML_EL_FIELD + "[@" + XML_ATTR_ID
				+ "='" + fieldId + "']";

		return xpath;
	}

	/**
	 * Get component XPath Expression to read configuration.
	 * 
	 * @param componentId
	 * @return
	 */
	public String getComponentXPathConf(String fieldXPath, String componentId) {
		String xpath = fieldXPath + "/" + XML_EL_COMPONENT + "[@" + XML_ATTR_ID
				+ "='" + componentId + "']";

		return xpath;
	}

	/**
	 * Get subcomponent XPath Expression to read configuration.
	 * 
	 * @param subcomponentId
	 * @return
	 */
	public String getSubComponentXPathConf(String componentXPath,
			String subcomponentId) {
		String xpath = componentXPath + "/" + XML_EL_SUBCOMPONENT + "[@"
				+ XML_ATTR_ID + "='" + subcomponentId + "']";

		return xpath;
	}

	/**
	 * Get name of given configuration XPath expression.
	 * 
	 * @param xPath
	 * @return
	 */
	public String getName(String xPath) {
		String result = config.getString(xPath + "/@" + XML_ATTR_NAME);

		if (result == null || result.equals("") || result.equals("null")) {
			return null;
		}

		return result;
	}

	/**
	 * Get information if given element is an attribute or single element basing
	 * on XPath configuration.
	 * 
	 * @param xPath
	 * @return
	 */
	public Boolean isAttribute(String xPath) {
		try {
			return config.getBoolean(xPath + "/@" + XML_ATTR_IS_ATTRIBUTE);
		} catch (NoSuchElementException ex) {
			// System.err.println("Element does not exist - assuming it's 'null'");
			return false;
		}
	}

}
