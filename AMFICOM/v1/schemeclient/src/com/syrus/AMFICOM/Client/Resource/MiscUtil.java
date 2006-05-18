/*-
 * $Id: MiscUtil.java,v 1.2 2005/08/08 11:58:06 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Resource;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:58:06 $
 * @module schemeclient
 */

public class MiscUtil {
	public static boolean validName(String name) {
		if (name != null && name.length() != 0)
			return true;
		return false;
	}
}
