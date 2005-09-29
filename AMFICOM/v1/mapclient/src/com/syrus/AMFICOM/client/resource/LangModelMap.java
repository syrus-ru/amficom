/*-
 * $Id: LangModelMap.java,v 1.3 2005/09/29 10:58:28 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * модуль организации многоязыковой поддержки для модуля Редактор топологических
 * схем клиентской части ПО АМФИКОМ
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/29 10:58:28 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public final class LangModelMap {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.client.resource.map";
	private static final ResourceBundle RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMap() {
		// empty
	}

	public static String getString(String keyName) {
		String searchKey = keyName.replaceAll(" ", "_");
		String string = "";
		try {
			string = RESOURCE_BUNDLE.getString(searchKey);
		} catch(MissingResourceException e) {
			string = "!" + searchKey + "!";

			try {
				String s = "key '" + searchKey + "' not found";
				throw new Exception(s);
			} catch(Exception exc) {
				// System.out.println(exc.getMessage());
				exc.printStackTrace();
			}
		}
		return string;
	}
}
