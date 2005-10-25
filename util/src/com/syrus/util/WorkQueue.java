/*-
* $Id: WorkQueue.java,v 1.1 2005/10/25 16:02:50 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.util;

import java.util.LinkedList;
import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/25 16:02:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module util
 */
public abstract class WorkQueue {
	final List<Runnable> queue = new LinkedList<Runnable>();
	boolean stopped = false;
	
	protected WorkQueue() {
		new WorkerThread().start();
	}
	
	public final void enqueue(final Runnable workItem) {
		synchronized (this.queue) {
			this.queue.add(workItem);
			this.queue.notify();
		}
	}
	
	protected abstract void processingItem(final Runnable workItem)
	throws InterruptedException;
	
	private class WorkerThread extends Thread {

		@Override
		public void run() {
			// main loop
			while (true) { 
				Runnable workItem = null;
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
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}
