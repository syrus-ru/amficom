/*-
 * $Id: StringToValueConverter.java,v 1.1 2006/07/02 18:42:38 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */


package com.syrus.AMFICOM.general;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/07/02 18:42:38 $
 * @module event
 */
public interface StringToValueConverter {
	String valueToString(final String key, final Object value);

	Object stringToValue(final String key, final String stringValue);
}
