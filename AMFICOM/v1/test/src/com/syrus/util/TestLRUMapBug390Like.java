package com.syrus.util;
import com.syrus.util.LRUMap;
import com.syrus.util.LRUMap.Retainable;

/*-
 * $Id: TestLRUMapBug390Like.java,v 1.1 2006/02/22 16:11:59 saa Exp $
 * 
 * Copyright ╘ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * Арсений утверждает, что этот тест уже включен в TestLRUMap :)
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/02/22 16:11:59 $
 * @module
 */
public class TestLRUMapBug390Like {
	protected LRUMap lrumap;
	private volatile int countM = 0;
	private volatile int countA = 0;
	private TestLRUMapBug390Like() {
		this.lrumap = new LRUMap();
		new Thread(new Modifier()).start();
//		new Thread(new Modifier()).start();
		new Thread(new Accessor()).start();
//		new Thread(new Accessor()).start();
		for (;;) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// @todo Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("countA " + countA + "; countM " + countM);
		}
		
	}

	private class Modifier implements Runnable {
		public void run() {
			Retainable retainable = new Retainable() {
				public boolean retain() {
					return false;
				}
			};

			for(;;) {
				countM++;
				TestLRUMapBug390Like.this.lrumap.put(Object.class, retainable);
				TestLRUMapBug390Like.this.lrumap.remove(Object.class);
			}
		}
	}

	private class Accessor implements Runnable {
		public void run() {
			for(;;) {
				countA++;
				TestLRUMapBug390Like.this.lrumap.get(Object.class);
			}
		}
	}

	public static void main(String[] args) {
		new TestLRUMapBug390Like();
	}
}
