/*
 * $Id: Fifo.java,v 1.1 2004/08/05 12:12:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/05 12:12:39 $
 * @author $Author: arseniy $
 * @module util
 */

public class Fifo implements Serializable {
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

	public int getNumber() {
		return this.number;
	}
}