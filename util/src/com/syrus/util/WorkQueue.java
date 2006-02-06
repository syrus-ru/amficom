/*-
* $Id: WorkQueue.java,v 1.2 2005/11/08 08:10:25 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.util;

import java.util.LinkedList;
import java.util.List;


/**
 * WorkQueue from &laquo;Effective Java&raquo; by Josh Bloch
 * 
 * @version $Revision: 1.2 $, $Date: 2005/11/08 08:10:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module util
 */
public abstract class WorkQueue<T> {
	final List<T> queue = new LinkedList<T>();
	boolean stopped = false;
	
	protected WorkQueue() {
		new WorkerThread().start();
	}
	
	public final void enqueue(final T workItem) {
		synchronized (this.queue) {
			this.queue.add(workItem);
			this.queue.notify();
		}
	}
	
	protected abstract void processingItem(final T workItem)
	throws InterruptedException;
	
	private class WorkerThread extends Thread {

		@Override
		public void run() {
			// main loop
			while (true) { 
				T workItem = null;
				synchronized (WorkQueue.this.queue) {
					try {
						while (WorkQueue.this.queue.isEmpty() && !WorkQueue.this.stopped) {							
								WorkQueue.this.queue.wait();							
						}
					} catch (final InterruptedException e) {
						return;
					}
					
					if (WorkQueue.this.stopped) {
						return;
					}
					
					workItem = WorkQueue.this.queue.remove(0);
				}
				
				try {
					processingItem(workItem);
				} catch (final InterruptedException e) {
					return;
				}
			}
		}
	}
}
