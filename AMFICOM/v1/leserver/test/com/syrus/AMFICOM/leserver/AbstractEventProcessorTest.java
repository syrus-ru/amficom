/*-
 * $Id: AbstractEventProcessorTest.java,v 1.1 2006/05/15 11:57:32 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/05/15 11:57:32 $
 * @module leserver
 */
final class AbstractEventProcessorTest {
	private AbstractEventProcessorTest() {
		assert false;
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		final Object lock = new Object();
		final Thread blocker = new Thread("Blocker") {
			@Override
			public void run() {
				synchronized (lock) {
					try {
						System.out.print(this.getName() + " running: ");
						for (int i = 0; i < 10; i++) {
							System.out.print('.');
							sleep(1000);
						}
						System.out.println();
					} catch (final InterruptedException ie) {
						/*
						 * Never.
						 */
						assert false;
					}
				}
			}
		};
		blocker.setDaemon(true);
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Runnable() {
			public void run() {
				System.out.println("Task #1 is running...");
			}
		});

		blocker.start();

		executorService.submit(new Runnable() {
			public void run() {
				/*
				 * Let blocker start prior to this task.
				 */
				Thread.yield();

				synchronized (lock) {
					System.out.println("Task #2 is running...");
				}
			}
		});
		executorService.submit(new Runnable() {
			public void run() {
				System.out.println("Task #3 is running...");
			}
		});
		executorService.submit(new Runnable() {
			public void run() {
				System.out.println("Task #4 is running...");
			}
		});

		/*
		 * Let executor start prior to its shutdown
		 */
		Thread.yield(); 
		final List<Runnable> runnables = executorService.shutdownNow();
		final long t0 = System.nanoTime();
		Runtime.getRuntime().addShutdownHook(new Thread("MainShutdown") {
			@Override
			public void run() {
				final long t1 = System.nanoTime();
				System.out.println(runnables.size() + " task(s) remain unfinished.");
				System.out.println("Has waited for executor to shutdown for " + (t1 - t0) / 1000000 / 1e3 + " second(s) after main shutdown.");
			}
		});
	}
}
