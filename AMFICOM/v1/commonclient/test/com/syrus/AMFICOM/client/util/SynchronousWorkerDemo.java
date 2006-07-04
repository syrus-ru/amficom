/*-
 * $Id: SynchronousWorkerDemo.java,v 1.2 2006/07/04 14:55:48 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

/**
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/07/04 14:55:48 $
 * @module
 */
public class SynchronousWorkerDemo {

	/**
	 * Тест-демонстрация
	 */
	public static void main(String[] args) {
		testPerformance();
		demo0();
		// these demos may depend on statusBar
//		demo1();
//		demo2();
	}

	private static void demo0() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new AbstractSynchronousWorker<Object>(null) {
						@Override
						public Object construct() throws InterruptedException {
							Thread.sleep(3000);
							return null;
						}}.execute();
				} catch (ExecutionException e) {
					// @todo Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}

	private static void demo1() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new SynchronousWorker<Object>(
							null, "Ожидание ", "Подождите...", true) {
						@Override
						public Object construct() throws InterruptedException {
							Thread.sleep(700);
							for (int i = 0; i < 10; i++) {
								Thread.sleep(300);
								setTitleAndText(null, "" + i);
								setPercents(i * 10);
							}
							return null;
						}}.execute();
				} catch (ExecutionException e) {
					// @todo Auto-generated catch block
					e.printStackTrace();
				}
			}});
	}

	private static void demo2() {
		for (int i = 0; i < 8; i++) {
			final int iFinal = i;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
				try {
					Object result = new SynchronousWorker<Object>(
							null, "Ожидание " + iFinal, "Подождите...", true) {
						@Override
						public Object construct() {
							try {
								// некоторые задачи вызовут всплытие диалога,
								// а некоторые - нет.
								Thread.sleep(iFinal/2 % 2 == 0 ? 300 : 20);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
//						throw new Error();
							return "Done " + iFinal;
						}}.execute();
						System.out.println(result);
				} catch (ExecutionException e) {
					// @todo Auto-generated catch block
					e.printStackTrace();
				}
				}
			});
		}
	}

	private static void testPerformance() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Starting measurements...");
				final Executor immediateExecutor = new Executor() {
					public void execute(Runnable command) {
						command.run();
					}
				};

				final ExecutorService singleThreadExecutor =
					Executors.newSingleThreadExecutor();

				// выполняем все немедленно, полностью блокируя UI
				// (приведено для сравнения, не имеет практической значимости)
				measurePerformance(immediateExecutor);

				// однопоточный исполнитель
				measurePerformance(singleThreadExecutor);

				// создание каждый раз нового потока
				measurePerformance(null);

				singleThreadExecutor.shutdown();
			}
		});
	}
	/**
	 * should be invoked from AWT EQ only
	 */
	static void measurePerformance(final Executor executor) {
		try {
			final int count = 1000;
			long t0 = System.nanoTime();
			for (int i = 0; i < count; i++) {
				final int iFinal = i;
				new SynchronousWorker<Object>(executor) {
					@Override
					public Object construct() {
						return "Done testPerformance #" + iFinal;
					}
				}.execute();
			}
			long t1 = System.nanoTime();

			System.out.println("Average RTT: "
					+ (t1 - t0) / count / 1000 + " us for executor "
					+ (executor == null ? null : executor.getClass().getName()));
		} catch (ExecutionException e) {
			// @todo Auto-generated catch block
			e.printStackTrace();
		}
	}
}
