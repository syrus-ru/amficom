/*-
 * $Id: QueueTest.java,v 1.3 2006/04/04 06:47:02 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.concurrent;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2006/04/04 06:47:02 $
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
		private List<T> queueEntries = Collections.synchronizedList(new LinkedList<T>());

		private Set<Thread> waitingProducers = Collections.synchronizedSet(new HashSet<Thread>());

		private Set<Thread> waitingConsumers = Collections.synchronizedSet(new HashSet<Thread>());

		private final Object putLock = new Object();

		private final Object getLock = new Object();

		private final int maximumSize;

		private Queue(final int size) {
			this.maximumSize = size;
		}

		Queue() {
			this(Integer.MAX_VALUE);
		}

		private String getWaitingProducers() {
			final StringBuilder stringBuilder = new StringBuilder("[");
			synchronized (this.waitingProducers) {
				for (final Thread producer : this.waitingProducers) {
					final String name = producer.getName();
					stringBuilder.append(stringBuilder.length() == 1 ? name : ", " + name);
				}
			}
			stringBuilder.append(']');
			return stringBuilder.toString();
		}
		
		private String getWaitingConsumers() {
			final StringBuilder stringBuilder = new StringBuilder("[");
			synchronized (this.waitingConsumers) {
				for (final Thread consumer : this.waitingConsumers) {
					final String name = consumer.getName();
					stringBuilder.append(stringBuilder.length() == 1 ? name : ", " + name);
				}
			}
			stringBuilder.append(']');
			return stringBuilder.toString();
		}

		void put(final T queueEntry) {
			try {
				synchronized (this.putLock) {
					while (this.queueEntries.size() == this.maximumSize) {
						final Thread producer = Thread.currentThread();
						assert !this.waitingProducers.contains(producer);
						this.waitingProducers.add(producer);
						this.putLock.wait();
						assert this.waitingProducers.contains(producer);
						this.waitingProducers.remove(producer);
					}
				}
			} catch (final InterruptedException ie) {
				ie.printStackTrace();
			}

			synchronized (this.queueEntries) {
				final int currentSize = this.queueEntries.size();
				assert currentSize < this.maximumSize :
						"currentSize: " + currentSize
						+ "; producers: " + this.getWaitingProducers()
						+ "; consumers: " + this.getWaitingConsumers();
				this.queueEntries.add(queueEntry);
				synchronized (this.getLock) {
					this.getLock.notify();
				}
			}
		}

		Set<T> getAll() {
			try {
				synchronized (this.getLock) {
					while (this.queueEntries.size() == 0) {
						final Thread consumer = Thread.currentThread();
						assert !this.waitingConsumers.contains(consumer);
						this.waitingConsumers.add(consumer);
						this.getLock.wait();
						assert this.waitingConsumers.contains(consumer);
						this.waitingConsumers.remove(consumer);
					}
				}
			} catch (final InterruptedException ie) {
				ie.printStackTrace();
			}

			synchronized (this.queueEntries) {
				final int currentSize = this.queueEntries.size();
				assert currentSize > 0 :
						"currentSize: " + currentSize
						+ "; producers: " + this.getWaitingProducers()
						+ "; consumers: " + this.getWaitingConsumers();
				final Set<T> returnValue = new HashSet<T>(this.queueEntries);
				this.queueEntries.clear();
				synchronized (this.putLock) {
					this.putLock.notify();
				}

				return Collections.unmodifiableSet(returnValue);
			}
		}

//		int getCurrentSize() {
//			return this.queueEntries.size();
//		}

		int getMaximumSize() {
			return this.maximumSize;
		}
	}

	private static class Consumer implements Runnable {
		private final Queue<QueueEntry> queue;

		Consumer(final Queue<QueueEntry> queue) {
			this.queue = queue;
		}

		private void consume() {
			for (final QueueEntry queueEntry : this.queue.getAll()) {
//				System.err.println("CONSUMED: " + queueEntry + "; size: " + this.queue.getCurrentSize());
				System.err.println("CONSUMED: " + queueEntry);
			}
		}

		public void run() {
			while (true) {
				consume();
				try {
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

		private void produce() {
			final QueueEntry queueEntry = new QueueEntry();
			this.queue.put(queueEntry);
//			System.err.println("PRODUCED: " + queueEntry + "; size: " + this.queue.getCurrentSize());
			System.err.println("PRODUCED: " + queueEntry);
		}

		public void run() {
			while (true) {
				produce();
				try {
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
