/*
 * $Id: Namable.java,v 1.4 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:27:25 $
 * @module general
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
