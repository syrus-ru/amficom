/*
 * $Id: ProgressThread.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import java.io.*;

/**
 * @bug interrupt() method doesn't stop the thread if it's not sleeping (i. e.
 *      printing smth to <code>out</code>). However, this will never be the
 *      case if <code>out</code> is <code>(PrintWriter) null</code>. Moreover,
 *      internal implementation of I/O operations may block on wait(), join()
 *      or sleep();
 *
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 */
final class ProgressThread extends Thread {
	private long timeInterval;

	private PrintWriter out;

	private ProgressThread() {
	}

	/**
	 * If this constructor is used, no progress will be displayed.
	 */
	ProgressThread(long timeInterval) {
		this(timeInterval, (PrintWriter) null);
	}

	/**
	 * @param out the PrintStream which output will be sent to. If null, a
	 *            NullPointerException is thrown.
	 */
	ProgressThread(long timeInterval, PrintStream out) {
		this(timeInterval, new PrintWriter(out));
	}

	ProgressThread(long timeInterval, PrintWriter out) {
		this.timeInterval = timeInterval;
		this.out = out;
		setName("ProgressThread");
	}

	public void run() {
		if (out == null)
			try {
				/*
				 * Clear the interrupted status or sleep() will fail (in case
				 * interrupt() was invoked onto a running thread). Both sleep()
				 * and interrupted() invocations must go within run() method,
				 * as these methods are static.
				 */
				interrupted();
				sleep(timeInterval);
			} catch (InterruptedException ie) {
				;
			}
		else
			synchronized (out) {
				try {
					final long count = timeInterval / 1000;
					for (long l = 0; l < count; l ++) {
						interrupted();
						sleep(1000);
						out.print('.');
						out.flush();
					}
					interrupted();
					sleep(timeInterval - count * 1000);
				} catch (InterruptedException ie) {
					;
				} finally {
					out.println();
					out.flush();
				}
			}
	}

//	public static void main(String args[]) {
//		ProgressThread progressThread = new ProgressThread(10000, System.out);
//		progressThread.start();
//		try {
//			progressThread.join();
//		} catch (InterruptedException ie) {
//		}
//	}
}
