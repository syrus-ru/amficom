/*-
 * $Id: LangModelMap.java,v 1.4 2005/09/29 11:32:36 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/09/29 11:32:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public final class LangModelMap {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.client.resource.map"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(BUNDLE_NAME);

	private LangModelMap() {
		// empty
	}

	public static String getString(String keyName) {
		String searchKey = keyName.replaceAll(" ", "_");  //$NON-NLS-1$//$NON-NLS-2$
		String string = ""; //$NON-NLS-1$
		try {
			string = RESOURCE_BUNDLE.getString(searchKey);
		} catch(MissingResourceException e) {
			string = "!" + searchKey + "!";  //$NON-NLS-1$//$NON-NLS-2$

			try {
				String s = "key '" + searchKey + "' not found";  //$NON-NLS-1$//$NON-NLS-2$
				throw new Exception(s);
			} catch(Exception exc) {
				// System.out.println(exc.getMessage());
				exc.printStackTrace();
			}
		}
		return string;
	}
}
