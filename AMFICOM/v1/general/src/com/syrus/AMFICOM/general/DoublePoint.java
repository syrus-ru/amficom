/**
 * $Id: DoublePoint.java,v 1.1 2005/08/12 12:24:35 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.general;

/**
 * Пара вещественных чисел, соответствующих географическим координатам.
 * Класс необходим для работы с географическими объектами, их отображения,
 * конвертации координат.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 12:24:35 $
 * @module general
 */
public class DoublePoint implements Cloneable {

	/**
	 * Координата, соответствующая долготе (longitude)
	 */
	private double x;

	/**
	 * Координата, соответствующая широте (latitude)
	 */
	private double y;

	/**
	 * empty default constructor.
	 */
	public DoublePoint() {
		//empty
	}

	public DoublePoint(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setLocation(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "DoublePoint[" + this.x + ", " + this.y + "]";
	}

	public void setLocation(final DoublePoint p) {
		setLocation(p.getX(), p.getY());
	}

	public static double distanceSq(final double X1, final double Y1, final double X2, final double Y2) {
		final double dX = X1 - X2;
		final double dY = Y1 - Y2;
		return (dX * dX + dY * dY);
	}

	public static double distance(final double X1, final double Y1, final double X2, final double Y2) {
		final double dX = X1 - X2;
		final double dY = Y1 - Y2;
		return Math.sqrt(dX * dX + dY * dY);
	}

	public double distanceSq(final double px, final double py) {
		final double dX = px - this.getX();
		final double dY = py - this.getY();
		return (dX * dX + dY * dY);
	}

	public double distanceSq(final DoublePoint pt) {
		final double px = pt.getX() - this.getX();
		final double py = pt.getY() - this.getY();
		return (px * px + py * py);
	}

	public double distance(final double px, final double py) {
		final double dX = px - this.getX();
		final double dY = py - this.getY();
		return Math.sqrt(dX * dX + dY * dY);
	}

	public double distance(final DoublePoint pt) {
		double px = pt.getX() - this.getX();
		double py = pt.getY() - this.getY();
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
	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(getX());
		bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DoublePoint) {
			final DoublePoint p2d = (DoublePoint) obj;
			return (this.getX() == p2d.getX()) && (this.getY() == p2d.getY());
		}
		return super.equals(obj);
	}
}
