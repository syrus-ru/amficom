/*-
 * $Id: MailTest.java,v 1.1 2005/11/13 00:51:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.util.Application;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/13 00:51:51 $
 * @module leserver
 */
final class MailTest {
	public static void main(final String args[]) {
		Application.init("leserver");
		SimpleMailer.sendMail(
				(args.length == 1 && args[0] != null)
						? args[0]
						: "bass@localhost",
				"Sample Subject",
				"Sample body.");
	}
}
