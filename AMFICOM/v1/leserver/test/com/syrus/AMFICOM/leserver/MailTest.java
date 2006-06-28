/*-
 * $Id: MailTest.java,v 1.3 2006/05/17 18:31:26 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.SEVERE;

import javax.mail.MessagingException;

import com.syrus.util.Application;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/05/17 18:31:26 $
 * @module leserver
 */
final class MailTest {
	public static void main(final String args[]) {
		Application.init("leserver");
		try {
			SimpleMailer.sendMail(
					(args.length == 1 && args[0] != null)
							? args[0]
							: "bass@localhost",
					"Sample Subject",
					"Sample body.",
					"Sample body.");
		} catch (MessagingException me) {
			while (me != null) {
				Log.debugMessage(me, SEVERE);
				final Exception nextException = me.getNextException();
				if (nextException instanceof MessagingException
						|| nextException == null) {
					me = (MessagingException) nextException;
					continue;
				}
				Log.debugMessage(nextException, SEVERE);
				break;
			}
		}
	}
}
