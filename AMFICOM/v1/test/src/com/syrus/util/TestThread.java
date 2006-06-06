/*-
 * $Id: TestThread.java,v 1.1.2.1 2006/06/06 15:48:03 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.DatabaseCommonTest;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/06 15:48:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestThread extends TestCase {

	public TestThread(final String name) {
		super(name);
	}

	public static Test suite() {
		final DatabaseCommonTest commonTest = new DatabaseCommonTest();
		commonTest.addTestSuite(TestThread.class);
		return commonTest.createTestSetup();
	}


	class Producer implements Runnable {
		private final BlockingQueue<Object> blockingQueue;

		Producer(final BlockingQueue<Object> blockingQueue) {
			this.blockingQueue = blockingQueue;
		}

		public void run() {
			while (true) {
				try {
					this.blockingQueue.put(this.produce());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private Object produce() {
			return new Object();
		}
	}

	class Consumer implements Runnable {
		private final BlockingQueue<Object> blockingQueue;

		Consumer(final BlockingQueue<Object> blockingQueue) {
			this.blockingQueue = blockingQueue;
		}

		public void run() {
			while (true) {
				try {
					this.consume(this.blockingQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void consume(final Object object) {
			System.out.println(object);
		}
	}

	public void testBlockingQueue() {
		final BlockingQueue<Object> blockingQueue = new SynchronousQueue<Object>();
		final Producer producer = new Producer(blockingQueue);
		final Consumer consumer1 = new Consumer(blockingQueue);
		final Consumer consumer2 = new Consumer(blockingQueue);
		new Thread(producer).start();
		new Thread(consumer1).start();
		new Thread(consumer2).start();
	}


	public void _testInterrupted() {
		final Worker worker = new Worker(1);
		worker.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		worker.interrupt();
		System.out.println("Interrupted: " + worker.isInterrupted());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	class Worker extends Thread {
		private volatile boolean running;

		Worker(final int number) {
			super("Worker-" + number);
			this.running = true;
		}

		@Override
		public void run() {
			while (this.running) {
				System.out.println(super.getName());
				try {
					sleep(100000);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
					return;
				}
			}
		}

		void shutdown() {
			this.running = false;
		}
	}


	public void _testOpenCursors() throws SQLException {
		final String sql = "SELECT sysdate FROM Dual";
		final Connection connection = DatabaseConnection.getConnection();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			final Statement statement = connection.createStatement();
			final ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				System.out.println(i + ": " + resultSet.getTimestamp(1));
			} else {
				throw new SQLException();
			}
			statement.close();
		}
	}
}
