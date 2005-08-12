/**
 * $Id: IntPoint.java,v 1.1 2005/08/12 12:15:14 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.HashCodeGenerator;

/**
 * Пара целых чисел, соответствующих, например, координатам объекта в
 * целочисленной двумерной плоскости.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 12:15:14 $
 * @module
 */
public class IntPoint implements Cloneable {

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
