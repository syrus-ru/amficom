/*-
 * $Id: MiscUtil.java,v 1.2 2005/09/05 12:26:27 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/09/05 12:26:27 $
 * @module schemeclient_v1
 */

public class MiscUtil {
	public static boolean validName(String name) {
		return (name != null && name.length() != 0);
	}
}
