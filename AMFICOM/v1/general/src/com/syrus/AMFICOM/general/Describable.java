/*
 * $Id: Describable.java,v 1.3 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:27:25 $
 * @module general
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
