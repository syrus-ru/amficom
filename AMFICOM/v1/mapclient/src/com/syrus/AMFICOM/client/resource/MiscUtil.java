/*-
 * $Id: MiscUtil.java,v 1.1 2005/06/06 12:52:29 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/06/06 12:52:29 $
 * @module schemeclient_v1
 */

public class MiscUtil {
	public static boolean validName(String name) {
		if (name != null && name.length() != 0)
			return true;
		return false;
	}
}
