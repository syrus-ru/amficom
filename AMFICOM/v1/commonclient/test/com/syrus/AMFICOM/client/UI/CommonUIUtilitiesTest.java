/*-
 * $Id: CommonUIUtilitiesTest.java,v 1.1 2006/05/18 19:32:18 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/05/18 19:32:18 $
 * @module commonclient
 */
final class CommonUIUtilitiesTest {
	private CommonUIUtilitiesTest() {
		assert false;
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		System.out.println(CommonUIUtilities.convertToHTMLString("\"\n'\n&\n<\n>\n"));
	}
}
