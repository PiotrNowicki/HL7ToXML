package com.piotrnowicki.hl7toxml.bll.config;

/**
 * System-wide configuration.
 * 
 * @author Piotr Nowicki
 * 
 */
public class SystemConfig {

	/**
	 * Directory paths
	 */
	public static final String XML_OUTPUT = "output/";
	public static final String HL7_INPUT = "input/";
	public static final String HL7_PARSED = "input_parsed/";
	public static final String CONFIG_FILE = "config.xml";

	/**
	 * Result XML settings
	 */
	public static final String XML_ROOT_NAME = "HL7XML";
	public static final String CONFIG_ROOT_NAME = "HL7XML_CONFIG";
	public static final String XML_NS = "http://www.put.poznan.pl/hl7toxml";

	/**
	 * Result XML elements
	 */
	public static final String DEFAULT_FIELD_PREFIX = "field";
	public static final String DEFAULT_COMPONENT_PREFIX = "comp";
	public static final String DEFAULT_SUBCOMPONENT_PREFIX = "subcomp";

	/**
	 * XML Config - system wide configuration
	 */
	public static final String XML_CONF = "systemConfig";
	public static final String XML_CONF_OPTION = "option";
	public static final String XML_CONF_OPTION_OMIT_UNDEF = "omitNonDefined";

	/**
	 * XML Config - elements
	 */
	public static final String XML_EL_MESSAGE = "message";
	public static final String XML_EL_SEGMENT = "segment";
	public static final String XML_EL_FIELD = "field";
	public static final String XML_EL_COMPONENT = "component";
	public static final String XML_EL_SUBCOMPONENT = "subcomponent";

	/**
	 * XML Config - attributes
	 */
	public static final String XML_ATTR_ID = "id";
	public static final String XML_ATTR_SUBID = "subid";
	public static final String XML_ATTR_IS_DEFAULT = "isDefault";
	public static final String XML_ATTR_IS_REQUIRED = "isRequired";
	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_IS_ATTRIBUTE = "isAttribute";

}
