/*
 * $Id: Fifo.java,v 1.3 2004/08/06 09:24:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.io.Serializable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/06 09:24:44 $
 * @author $Author: arseniy $
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
}
