package com.piotrnowicki.hl7toxml.bll.config;

import com.piotrnowicki.hl7toxml.bll.ConfigHelper;
import com.piotrnowicki.hl7toxml.model.Field;

/**
 * Single Message configuration.
 * 
 * @author Piotr Nowicki
 * 
 */
public class MessageConfig {

	public static String FIELD_SEPARATOR = "\\|";
	public static String COMPONENT_SEPARATOR = "\\^";
	public static String SUBCOMPONENT_SEPARATOR = "\\&";
	public static String REPEAT_SEPARATOR = "\\~";
	public static String ESCAPE_CHAR = "\\";
	public static String MESSAGE_XPATH;

	/**
	 * Initialize message config.
	 * 
	 */
	public static void initConfig(String msgHeader) {
		FIELD_SEPARATOR = "\\" + msgHeader.substring(3, 4);
		COMPONENT_SEPARATOR = "\\" + msgHeader.substring(4, 5);
		REPEAT_SEPARATOR = "\\" + msgHeader.substring(5, 6);
		ESCAPE_CHAR = "\\" + msgHeader.substring(6, 7);
		SUBCOMPONENT_SEPARATOR = "\\" + msgHeader.substring(7, 8);
	}

	/**
	 * Initialize message XPath (it's unchanged for whole message)
	 * 
	 * @param f
	 */
	public static void setMessageXpath(Field f) {
		String id;
		String subId;

		if (f.getComponents() != null) {
			id = f.getComponents()[0].getValue();
			subId = f.getComponents()[1].getValue();
		} else {
			id = f.getValue();
			subId = null;
		}

		MESSAGE_XPATH = ConfigHelper.getInstance().getMessageXPathConf(id,
				subId);
	}
}
