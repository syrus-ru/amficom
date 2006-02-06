/*
 * $Id: Fifo.java,v 1.11 2005/12/02 11:00:09 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.Serializable;

/**
 * @version $Revision: 1.11 $, $Date: 2005/12/02 11:00:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class Fifo implements Serializable {
	private static final long serialVersionUID = -2241622428099411636L;
	public static final int SIZE = 10;

	private Object[] array;
	private int number;

	public Fifo() {
		this (SIZE);
	}

	public Fifo(final int size) {
		if (size > 0) {
			this.array = new Object[size];
		} else {
			throw new IllegalArgumentException("Illegal size: " + size);
		}
		this.number = 0;
	}

	public int capacity() {
		return this.array.length;
	}

	public Object push(final Object obj) {
		final Object ret = this.array[this.array.length - 1];
		for (int i = this.array.length - 1; i > 0; i--) {
			this.array[i] = this.array[i - 1];
		}
		this.array[0] = obj;
		if (this.number < this.array.length) {
			this.number++;
		}
		return ret;
	}

	public Object remove() {
		if (this.number > 0) {
			final Object ret = this.array[this.number - 1];
			this.array[this.number - 1] = null;
			this.number--;
			return ret;
		}
		return null;
	}

	public boolean contains(final Object obj) {
		return this.indexOf(obj) >= 0;
	}

	public int indexOf(final Object obj) {
		if (obj == null) {
			for (int i = 0; i < this.number; i++) {
				if (this.array[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < this.number; i++) {
				if (obj.equals(this.array[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getNumber() {
		return this.number;
	}
	
	/**
	 * <b>Do NOT use this metthod</b>.
	 * <p>This method can be used only in special cases, for example in seriallization.</p>
	 */
	Object[] getObjects() {
		return this.array;
	}

	/**
	 * <b>Do NOT use this metthod</b>.
	 * <p>This method can be used only in special cases, for example in seriallization.</p><p>Instead one may use {@link Fifo#push(Object)}</p>
	 */
	void populate(final Object[] objects) {
		if (objects == null) {
			throw new NullPointerException("Argument is null");
		}

		this.number = 0;
		for (int i = 0; i < objects.length && i < this.array.length; i++) {
			final Object object = objects[i];
			if (object == null) {
				break;
			}
			this.number++;
			this.array[i] = object;
		}
	}
}
