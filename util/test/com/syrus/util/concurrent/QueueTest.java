/*-
 * $Id: QueueTest.java,v 1.4 2006/04/04 08:10:33 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2006/04/04 08:10:33 $
 * @module util
 */
final class QueueTest {
	private static class QueueEntry {
		private static long currentL = 0;

		private static synchronized long newL() {
			return currentL++;
		}

		private long l;
		
		QueueEntry() {
			this.l = newL();
		}

		@Override
		public String toString() {
			return String.valueOf(this.l);
		}
	}

	private static class Queue<T> {
		private final BlockingQueue<T> queueEntries;

		private Queue(final int capacity) {
			this.queueEntries = new LinkedBlockingQueue<T>(capacity);
		}

		Queue() {
			this(Integer.MAX_VALUE);
		}

		void put(final T queueEntry) throws InterruptedException {
			this.queueEntries.put(queueEntry);
		}

		Set<T> takeAll() throws InterruptedException {
			final Set<T> returnValue = new HashSet<T>();
			returnValue.add(this.queueEntries.take());
			this.queueEntries.drainTo(returnValue);
			return Collections.unmodifiableSet(returnValue);
		}

		int size() {
			return this.queueEntries.size();
		}
	}

	private static class Consumer implements Runnable {
		private final Queue<QueueEntry> queue;

		Consumer(final Queue<QueueEntry> queue) {
			this.queue = queue;
		}

		private void consume() throws InterruptedException {
			for (final QueueEntry queueEntry : this.queue.takeAll()) {
				System.err.println("CONSUMED: " + queueEntry + "; size: " + this.queue.size());
			}
		}

		public void run() {
			while (true) {
				try {
					consume();
					Thread.sleep(10 * 1000);
				} catch (final InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	private static class Producer implements Runnable {
		private final Queue<QueueEntry> queue;

		Producer(final Queue<QueueEntry> queue) {
			this.queue = queue;
		}

		private void produce() throws InterruptedException {
			final QueueEntry queueEntry = new QueueEntry();
			this.queue.put(queueEntry);
			System.err.println("PRODUCED: " + queueEntry + "; size: " + this.queue.size());
		}

		public void run() {
			while (true) {
				try {
					produce();
					Thread.sleep(1 * 1000);
				} catch (final InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		try {
			assert false;
			System.err.println("Assertions are disabled -- rerun with -ea VM arg.");
			System.exit(1);
		} catch (final AssertionError ae) {
			// empty
		}

		final Queue<QueueEntry> queue = new Queue<QueueEntry>();

		final int producerCount = 10;
		final int consumerCount = 10;

		for (int i = 0; i < producerCount; i++) {
			new Thread(new Producer(queue), "Producer-" + i).start();
		}

		for (int i = 0; i < consumerCount; i++) {
			new Thread(new Consumer(queue), "Consumer-" + i).start();
		}
	}
}
