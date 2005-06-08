/*
 * $Id: SystemLogWriter.java,v 1.7 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public final class SystemLogWriter {
	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	static FileOutputStream fos;
	static PrintStream pstr;
	static PrintWriter pwr;

	static {
		try {
			fos = new FileOutputStream(".\\client.log", true);
//			pwr = new PrintWriter(fos);
			pstr = new PrintStream(fos);
			System.setOut(pstr);
		}
		catch (Exception e) {
			System.err.println("COULD NOT CREATE CLIENT LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			System.out.println("");
			System.out.println("-------------------------------------------------------");
			System.out.println("started " + sdf.format(new Date(System.currentTimeMillis())));
			System.out.println("-------------------------------------------------------");
			System.out.println("");
		}
		catch (Exception e) {
			System.err.println("COULD NOT WRITE CLIENT LOG FILE: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private SystemLogWriter() {
		assert false;
	}

	private static boolean initiated = false;

	public static void initialize() {
		if (initiated)
			return;

		initiated = true;
	}

	public static void closeLog() {
		try {
			System.out.println("");
			System.out.println("-------------------------------------------------------");
			System.out.println("closed " + sdf.format(new Date(System.currentTimeMillis())));
			System.out.println("-------------------------------------------------------");
			System.out.println("");
			System.out.close();
			try {
				fos.close();
			}
			catch (IOException ioe) {
				System.err.println("Exception: " + ioe.getMessage());
				ioe.printStackTrace();
			}
		}
		catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
