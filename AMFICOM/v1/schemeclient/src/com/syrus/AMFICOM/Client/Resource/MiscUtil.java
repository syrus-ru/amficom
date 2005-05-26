/*-
 * $Id: MiscUtil.java,v 1.1 2005/05/26 07:33:00 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Resource;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/05/26 07:33:00 $
 * @module schemeclient_v1
 */

public class MiscUtil {
	public static boolean validName(String name) {
		if (name != null && name.length() != 0)
			return true;
		return false;
	}
}
