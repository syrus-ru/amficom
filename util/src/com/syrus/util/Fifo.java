/*
 * $Id: Fifo.java,v 1.7 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.Serializable;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */

public class Fifo implements Serializable {
	private static final long serialVersionUID = -2241622428099411636L;
	public static final int SIZE = 10;

	private Object[] fifo;
	private int number;

	public Fifo() {
		this (SIZE);
	}

	public Fifo(int size) {
		if (size > 0)
			this.fifo = new Object[size];
		else
			throw new IllegalArgumentException("Illegal size: " + size);
		this.number = 0;
	}
	
	public int capacity(){
		return this.fifo.length;
	}

	public Object push(Object obj) {
		Object ret = this.fifo[this.fifo.length - 1];
		for (int i = this.fifo.length - 1; i > 0; i--) {
			this.fifo[i] = this.fifo[i - 1];
		}
		this.fifo[0] = obj;
		if (this.number < this.fifo.length)
			this.number ++;
		return ret;
	}

	public Object remove() {
		Object ret = this.fifo[this.number - 1];
		this.fifo[this.number - 1] = null;
		this.number --;
		return ret;
	}

	public boolean contains(Object obj) {
		return this.indexOf(obj) >= 0;
	}

	public int indexOf(Object obj) {
		if (obj == null) {
			for (int i = 0; i < this.number; i++)
				if (this.fifo[i] == null)
					return i;
		}
		else {
			for (int i = 0; i < this.number; i++)
				if (obj.equals(this.fifo[i]))
					return i;
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
	public Object[] getObjects() {
		return this.fifo;
	}

	/**
	 * <b>Do NOT use this metthod</b>.
	 * <p>This method can be used only in special cases, for example in seriallization.</p><p>Instead one may use {@link Fifo#push(Object)}</p>
	 */
	public void setObjects(Object[] objects) {
		this.fifo = objects;		
	}

	/**
	 * <b>Do NOT use this metthod</b>.
	 * <p>This method can be used only in special cases, for example in seriallization.</p>
	 */
	public void setNumber(int number) {
		this.number = number;
	}
}
