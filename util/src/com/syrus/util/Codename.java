/*-
 * $Id: Codename.java,v 1.2 2006/04/19 13:47:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/19 13:47:03 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Codename {

	/**
	 * @return Interned representation of string value.
	 * @see String#intern()
	 */
	String stringValue();
}
