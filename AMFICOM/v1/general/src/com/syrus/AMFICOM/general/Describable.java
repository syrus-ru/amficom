/*
 * $Id: Describable.java,v 1.2 2005/03/17 18:16:12 bass Exp $
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
public interface Describable extends Namable {
	/**
	 * @return this object&apos;s description, or empty string if none.
	 *         Never returns <code>null</code>s.
	 */
	String getDescription();

	/**
	 * @param description cannot be <code>null</code>. For this purpose,
	 *        supply an empty string as an argument.
	 */
	void setDescription(final String description);
}
