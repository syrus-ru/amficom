/*-
 * $Id: MiscUtil.java,v 1.3 2005/09/29 10:58:28 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/29 10:58:28 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapclient
 */
public class MiscUtil {
	public static boolean validName(String name) {
		return (name != null && name.length() != 0);
	}
}
