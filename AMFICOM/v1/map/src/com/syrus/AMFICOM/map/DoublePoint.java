/**
 * $Id: DoublePoint.java,v 1.3 2005/01/27 14:43:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

/**
 * Пара вещественных чисел, соответствующих географическим координатам.
 * Класс необходим для работы с географическими объектами, их отображения,
 * конвертации координат.
 *  
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */
public class DoublePoint implements Cloneable {

	/**
	 * Координата, соответствующая долготе (longitude)
	 */
	private double	x;

	/**
	 * Координата, соответствующая широте (latitude)
	 */
	private double	y;

	/**
	 * empty default constructor.
	 */
	public DoublePoint() {
	}

	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "DoublePoint[" + this.x + ", " + this.y + "]";
	}

	public void setLocation(DoublePoint p) {
		setLocation(p.getX(), p.getY());
	}

	public static double distanceSq(double X1, double Y1, double X2, double Y2) {
		X1 -= X2;
		Y1 -= Y2;
		return (X1 * X1 + Y1 * Y1);
	}

	public static double distance(double X1, double Y1, double X2, double Y2) {
		X1 -= X2;
		Y1 -= Y2;
		return Math.sqrt(X1 * X1 + Y1 * Y1);
	}

	public double distanceSq(double px, double py) {
		px -= getX();
		py -= getY();
		return (px * px + py * py);
	}

	public double distanceSq(DoublePoint pt) {
		double px = pt.getX() - this.getX();
		double py = pt.getY() - this.getY();
		return (px * px + py * py);
	}

	public double distance(double px, double py) {
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
	}

	public double distance(DoublePoint pt) {
		double px = pt.getX() - this.getX();
		double py = pt.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public int hashCode() {
		long bits = java.lang.Double.doubleToLongBits(getX());
		bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}

	public boolean equals(Object obj) {
		if (obj instanceof DoublePoint) {
			DoublePoint p2d = (DoublePoint) obj;
			return (getX() == p2d.getX()) && (getY() == p2d.getY());
		}
		return super.equals(obj);
	}
}
