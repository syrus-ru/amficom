/*
 * $Id: LangModelMeasurement.java,v 1.5 2005/06/17 12:38:56 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @version $ $, $Date: 2005/06/17 12:38:56 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class LangModelMeasurement {
	private static final String BUNDLE_NAME			= "com.syrus.AMFICOM.resource.measurement";

	private static final ResourceBundle	RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMeasurement(){
		//nothing
	}

	public static String getString(String keyName) {
		keyName = keyName.replaceAll(" ", "_");
		String string;
		try {
			string = RESOURCE_BUNDLE.getString(keyName);
		} catch (MissingResourceException e) {
			string = "!" + keyName + "!";
			try {
				String s = "key '"
						+ keyName
						+ "' "
						+ "not found";
				throw new Exception(s);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		return string;
	}

}
