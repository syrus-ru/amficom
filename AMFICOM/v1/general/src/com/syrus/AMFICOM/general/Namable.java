/*
 * $Id: Namable.java,v 1.2 2005/03/17 18:16:12 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 18:16:12 $
 * @module general_v1
 */
public interface Namable {
	/**
	 * @return this object&apos;s name. Never returns <code>null</code> s
	 *         or empty strings.
	 */
	String getName();

	/**
	 * @param name cannot be <code>null</code> or empty string.
	 */
	void setName(final String name);
}
