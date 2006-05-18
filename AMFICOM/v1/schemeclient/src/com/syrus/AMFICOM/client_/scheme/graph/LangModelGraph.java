/*-
 * $Id: LangModelGraph.java,v 1.3 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
 */

public class LangModelGraph {
	private static final String BUNDLE_NAME = "com.syrus.AMFICOM.client_.scheme.graph.messages";//$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private LangModelGraph() {
	}

	public static String getString(String key) {
		// TODO Auto-generated method stub
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
