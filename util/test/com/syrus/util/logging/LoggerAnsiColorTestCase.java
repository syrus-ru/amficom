/*-
 * $Id: LoggerAnsiColorTestCase.java,v 1.1 2006/05/24 15:15:47 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.logging;

import static com.syrus.util.Log.DEBUGLEVEL08;
import static com.syrus.util.Log.DEBUGLEVEL09;
import static com.syrus.util.Log.DEBUGLEVEL10;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.File;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import com.syrus.util.DefaultLogger;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/05/24 15:15:47 $
 * @module util
 */
public final class LoggerAnsiColorTestCase extends TestCase {
	public void test() {
		final String logDir = System.getProperty("java.io.tmpdir") + File.separatorChar
				+ ".amficom-" + System.getProperty("user.name");
		System.setProperty("amficom.logging.LogDebugLevel", "10");
		System.setProperty("amficom.logging.EchoDebug", Boolean.toString(true));
		System.setProperty("amficom.logging.LogPath", logDir);
		System.setProperty("amficom.logging.AllowLevelOutput", Boolean.toString(true));
		System.setProperty("amficom.logging.AllowAnsiColor", Boolean.toString(true));
		System.setProperty("amficom.logging.FullSte", Boolean.toString(true));
		System.setProperty("amficom.logging.StackTraceDataSource", "throwable");
		Log.setLogger(new DefaultLogger());

		System.out.println();

		final String message = "A quick brown fox jumps over the lazy dog";
		Log.debugMessage(message, SEVERE);
		Log.debugMessage(message, WARNING);
		Log.debugMessage(message, INFO);
		Log.debugMessage(message, CONFIG);
		Log.debugMessage(message, FINE);
		Log.debugMessage(message, FINER);
		Log.debugMessage(message, FINEST);
		Log.debugMessage(message, DEBUGLEVEL08);
		Log.debugMessage(message, DEBUGLEVEL09);
		Log.debugMessage(message, DEBUGLEVEL10);

		final Throwable t = new Throwable(message);
		Log.debugMessage(t, SEVERE);
		Log.debugMessage(t, WARNING);
		Log.debugMessage(t, INFO);
		Log.debugMessage(t, CONFIG);
		Log.debugMessage(t, FINE);
		Log.debugMessage(t, FINER);
		Log.debugMessage(t, FINEST);
		Log.debugMessage(t, DEBUGLEVEL08);
		Log.debugMessage(t, DEBUGLEVEL09);
		Log.debugMessage(t, DEBUGLEVEL10);

		System.out.println("... Sorry, I wrote a lot of logs under "
				+ logDir + "; please delete that junk logfiles.");
	}

	public static void main(final String args[]) {
		TestRunner.run(LoggerAnsiColorTestCase.class);
	}
}
