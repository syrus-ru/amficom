/*-
 * $Id: SchemeUtils.java,v 1.50 2006/06/16 10:27:31 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.util.Shitlet;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: bass $
 * @version $Revision: 1.50 $, $Date: 2006/06/16 10:27:31 $
 * @module scheme
 * @deprecated
 */
@Shitlet
@Deprecated
public final class SchemeUtils {
	private SchemeUtils() {
		assert false;
	}

	public static SchemeElement getTopLevelSchemeElement(final SchemeElement schemeElement) {
		SchemeElement top = schemeElement;
		while (top.getParentSchemeElement() != null) {
			top = top.getParentSchemeElement();
		}
		return top;
	}
}
