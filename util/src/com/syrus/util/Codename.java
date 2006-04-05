/*-
 * $Id: Codename.java,v 1.1 2006/04/05 09:44:07 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

/**
 * @version $Revision: 1.1 $, $Date: 2006/04/05 09:44:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface Codename {

	/**
	 * @return Interned representation of string value.
	 * @see {@link String#intern()}.
	 */
	String stringValue();
}
