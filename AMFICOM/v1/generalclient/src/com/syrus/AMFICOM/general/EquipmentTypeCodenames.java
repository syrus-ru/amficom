/*-
 * $Id: EquipmentTypeCodenames.java,v 1.1 2005/04/18 08:56:56 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 08:56:56 $
 * @module generalclient_v1
 */

public class EquipmentTypeCodenames {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.general.EquipmentTypeCodenames";//$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String	REFLECTOMETER	= "reflectometer";
	public static final String	SWITCH				= "switch";
	public static final String	MUFF					= "muff";
	public static final String	CABLE_PANEL 	= "cable_panel";

	public static final String	TRANSMITTER 	= "transmitter";
	public static final String	RECEIVER 			= "receiver";
	public static final String	MULTIPLEXOR 	= "multiplexor";
	public static final String	CROSS 				= "cross";
	public static final String	FILTER 				= "filter";
	
	public static final String	OTHER 				= "other"; //$NON-NLS-1$
	
	private EquipmentTypeCodenames() {
		// empty
	}

	public static String getName(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
