/*-
 * $Id: TestLRUMap.java,v 1.4 2006/04/25 10:27:50 arseniy Exp $
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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.util.LRUMap.Retainable;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/25 10:27:50 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestLRUMap extends TestCase {
	private static final String KEY_PREFIX = "key_";

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
			final String key = KEY_PREFIX + i;
			final Value value = new Value(i);
			lruMap.put(key, value);
		}
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void _testRemove() {
		lruMap.remove(KEY_PREFIX + "10");
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void _testPutAll() {
		final Map<String, Value> map = new HashMap<String, Value>();
		for (int i = 100; i < 200; i++) {
			map.put(KEY_PREFIX + i, new Value(i));
		}
		lruMap.putAll(map);
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}

	public void testConcurrent() {
		final Thread getThread = new Thread() {
			@Override
			public void run() {
				for (int i = 199; i >= 100; i--) {
					final String key = KEY_PREFIX + i;
					final Value value = lruMap.get(key);
					System.out.println("Get | Key: " + key + ", value: " + value);
				}
			}
		};
		final Thread putThread = new Thread() {
			@Override
			public void run() {
				for (int i = 100; i < 200; i++) {
					final String key = KEY_PREFIX + i;
					final Value value = new Value(i);
					System.out.println("Put | Key: " + key + ", value: " + value);
					lruMap.put(key, value);
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		putThread.start();
		getThread.start();

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
			if (key.equals(KEY_PREFIX + "30")) {
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

	public void __estRetain() {
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

		lruMap.put(KEY_PREFIX + "1001", new Value(1001));
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);

		for (final Map.Entry<String, Value> entry : lruMap.entrySet()) {
			final Value value = entry.getValue();
			if (value.isEven()) {
				value.setRetained(false);
			}
		}

		lruMap.put(KEY_PREFIX + "1002", new Value(1002));
		System.out.println("Size: " + lruMap.size());
		System.out.println("LRUMap: " + lruMap);
	}
}
