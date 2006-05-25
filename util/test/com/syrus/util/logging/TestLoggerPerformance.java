/*-
 * $Id: TestLoggerPerformance.java,v 1.3 2006/05/25 18:33:30 bass Exp $
 * 
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.logging;

import java.io.File;
import java.util.logging.Level;

import junit.framework.TestCase;

import com.syrus.util.DefaultLogger;
import com.syrus.util.Log;

/**
 * This is not an actual test. This is a measuring tool only.
 * 
 * @author saa
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/05/25 18:33:30 $
 * @module util/test
 */
public class TestLoggerPerformance extends TestCase {
	private long measureDtNanos() {
		long t0 = System.nanoTime();
		int count = 0;
		while (System.nanoTime() - t0 < 100000000L && count < 100000) {
			Log.debugMessage("Hello, log", Level.INFO);
			count++;
		}
		return (System.nanoTime() - t0) / count;
	}

	private double measureDtMillis() {
		return measureDtNanos() / 1000 / 1e3;
	}

	private String measureAverages() {
		return "dt: " + measureDtMillis() + ", " + measureDtMillis()
				+ ", " + measureDtMillis() + ", " + measureDtMillis()
				+ ", " + measureDtMillis() + ", " + measureDtMillis() + " ms";
	}

	private void measureAtConditions(final String allowLevel,
			final String allowColor,
			final String stackTrace) {
		System.setProperty("amficom.logging.AllowLevelOutput", allowLevel);
		System.setProperty("amficom.logging.AllowAnsiColor", allowColor);
		System.setProperty("amficom.logging.StackTraceDataSource", stackTrace);
		Log.setLogger(new DefaultLogger());
		Log.debugMessage("Hello", Level.INFO);
		System.out.printf("allowLevel: %6s; allowColor: %6s;  STDS: %10s | %s%n",
				allowLevel,
				allowColor,
				stackTrace,
				measureAverages());
	}

	public void test() {
		final String logDir = System.getProperty("java.io.tmpdir") + File.separatorChar
				+ ".amficom-" + System.getProperty("user.name");
		System.setProperty("amficom.logging.LogDebugLevel", "10");
		System.setProperty("amficom.logging.EchoDebug", "false");
		System.setProperty("amficom.logging.LogPath", logDir);
		measureAtConditions("false", "false", "none");
		measureAtConditions("true", "false", "none");
		measureAtConditions("false", "true", "none");
		measureAtConditions("false", "false", "throwable");
		measureAtConditions("false", "false", "thread");
		System.out.println("... Sorry, I wrote a lot of logs under " + logDir + ", " +
				"Please delete that junk logfiles.");
	}
}
