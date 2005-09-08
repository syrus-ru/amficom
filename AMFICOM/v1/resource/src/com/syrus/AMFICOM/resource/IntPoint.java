/*-
 * $Id: IntPoint.java,v 1.3 2005/09/08 09:07:29 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.io.Serializable;

import com.syrus.util.HashCodeGenerator;

/**
 * \u041f\u0430\u0440\u0430 \u0446\u0435\u043b\u044b\u0445 \u0447\u0438\u0441\u0435\u043b, \u0441\u043e\u043e\u0442\u0432\u0435\u0442\u0441\u0442\u0432\u0443\u044e\u0449\u0438\u0445, \u043d\u0430\u043f\u0440\u0438\u043c\u0435\u0440, \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u0430\u043c \u043e\u0431\u044a\u0435\u043a\u0442\u0430 \u0432
 * \u0446\u0435\u043b\u043e\u0447\u0438\u0441\u043b\u0435\u043d\u043d\u043e\u0439 \u0434\u0432\u0443\u043c\u0435\u0440\u043d\u043e\u0439 \u043f\u043b\u043e\u0441\u043a\u043e\u0441\u0442\u0438.
 *
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/09/08 09:07:29 $
 * @module resource
 */
public class IntPoint implements Cloneable, Serializable {

	private static final long	serialVersionUID	= -7524861510543511881L;

	public int x;

	public int y;

	public IntPoint() {
		// default constructor
	}

	public IntPoint(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setLocation(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "IntPoint[" + this.x + ", " + this.y + "]";
	}

	public void setLocation(final IntPoint p) {
		setLocation(p.getX(), p.getY());
	}

	public static double distanceSq(final int x1, final int y1, final int x2, final int y2) {
		final int dX = x1 - x2;
		final int dY = y1 - y2;
		return (dX * dX + dY * dY);
	}

	public static double distance(final int x1, final int y1, final int x2, final int y2) {
		final int dX = x1 - x2;
		final int dY = y1 - y2;
		return Math.sqrt(dX * dX + dY * dY);
	}

	public double distanceSq(final int px, final int py) {
		final int dX = px - this.getX();
		final int dY = py - this.getY();
		return (dX * dX + dY * dY);
	}

	public double distanceSq(final IntPoint pt) {
		final int px = pt.getX() - this.getX();
		final int py = pt.getY() - this.getY();
		return (px * px + py * py);
	}

	public double distance(final int px, final int py) {
		final int dX = px - this.getX();
		final int dY = py - this.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	public double distance(final IntPoint pt) {
		final int px = pt.getX() - this.getX();
		final int py = pt.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof IntPoint) {
			final IntPoint p2d = (IntPoint) obj;
			return (getX() == p2d.getX()) && (getY() == p2d.getY());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		final HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(this.x);
		codeGenerator.addInt(this.y);
		return codeGenerator.getResult();
	}
}
