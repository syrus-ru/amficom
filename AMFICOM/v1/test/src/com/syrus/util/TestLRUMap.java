/*-
 * $Id: TestLRUMap.java,v 1.2 2005/12/09 08:18:09 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.util.LRUMap.Retainable;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/09 08:18:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestLRUMap extends TestCase {

	private static class Value implements Retainable, Serializable {
		private static final long serialVersionUID = 8879807331759613030L;

		final int value;
		boolean isRetained;

		public Value(final int value) {
			this.value = value;
			this.isRetained = false;
		}

		public boolean retain() {
			return this.isRetained;
		}

		public void setRetained(final boolean isRetained) {
			this.isRetained = isRetained;
		}

		@Override
		public String toString() {
			return Integer.toString(this.value);
		}

		@Override
		public boolean equals(final Object object) {
			if (!(object instanceof Value)) {
				return false;
			}
			return ((Value) object).value == this.value;
		}

		public boolean isEven() {
			return this.value / 2.0 == Math.round(this.value / 2.0);
		}
	}

	private static LRUMap<String, Value> lruMap;

	public TestLRUMap(final String name) {
		super(name);
	}

	public static Test suite() {
		final TestSetup testSetup = new TestSetup(new TestSuite(TestLRUMap.class)) {

			@Override
			protected void setUp() {
				oneTimeSetUp();
			}

			@Override
			protected void tearDown() {
				oneTimeTearDown();
			}

		};
		return testSetup;
	}

	static void oneTimeSetUp() {
		final int capacity = 10;
		final long timeToLive = 5 * 1000 * 1000 * 1000; //5 sec
		lruMap = new LRUMap<String, Value>(capacity, timeToLive);
	}

	static void oneTimeTearDown() {
		//nothing
	}


	public void testPut() {
		final int size = 100;
		for (int i = 0; i < size; i++) {
			final String key = "key_" + i;
			final Value value = new Value(i);
			lruMap.put(key, value);
		}
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void _testRemove() {
		lruMap.remove("key_10");
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void testPutAll() {
		final Map<String, Value> map = new HashMap<String, Value>();
		for (int i = 100; i < 200; i++) {
			map.put("key_" + i, new Value(i));
		}
		lruMap.putAll(map);
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void _testIterator() {
		final Thread thread1 = new Thread() {
			@Override
			public void run() {
				for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
					System.out.println(" Thread 1 | Key: " + entry.getKey() + ", value: " + entry.getValue());
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		final Thread thread2 = new Thread() {
			@Override
			public void run() {
				for (int i = 100; i < 200; i++) {
					lruMap.put("key_" + i, new Value(i));
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread2.start();
		thread1.start();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("LRUMap: " + lruMap);
	}

	public void _testKeySet() {
		for (final Iterator<String> it = lruMap.keySet().iterator(); it.hasNext();) {
			final String key = it.next();
			if (key.equals("key_30")) {
				it.remove();
			}
		}
		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
		}
	}

	public void _testValues() {
		for (final Iterator<Value> it = lruMap.values().iterator(); it.hasNext();) { 
			final Value value = it.next();
			if (value.equals(new Value(20))) { 
				it.remove();
			}
		}
		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
		}
	}

	public void _testSerialize() throws IOException, ClassNotFoundException {
		final String filename = "lrumap";

		System.out.println("TO SERIALIZE:");
		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
		}
		final FileOutputStream fos = new FileOutputStream(filename);
		final ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(lruMap);

		final FileInputStream fis = new FileInputStream(filename);
		final ObjectInputStream is = new ObjectInputStream(fis);
		lruMap = (LRUMap<String, Value>) is.readObject();
		System.out.println("RESTORED:");
		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
		}
	}

	public void testRetain() {
		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			final Value value = entry.getValue();
			if (value.isEven()) {
				value.setRetained(true);
			}
		}

		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		lruMap.put("key_1001", new Value(1001));
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);

		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			final Value value = entry.getValue();
			if (value.isEven()) {
				value.setRetained(false);
			}
		}

		lruMap.put("key_1002", new Value(1002));
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}
}
