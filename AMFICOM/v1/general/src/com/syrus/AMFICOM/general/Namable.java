/*
 * $Id: Namable.java,v 1.3 2005/03/18 19:19:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/18 19:19:49 $
 * @module general_v1
 */
public interface Namable {
	/**
	 * @return this object&apos;s name. Never returns <code>null</code> s
	 *         or empty strings.
	 */
	String getName();

	/**
	 * @param name can be neither <code>null</code> nor an empty string.
	 */
	void setName(final String name);
}
