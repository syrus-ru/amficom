
package com.syrus.AMFICOM.map;

import com.syrus.util.HashCodeGenerator;

public class IntPoint implements Cloneable {

	public int	x;

	public int	y;

	public IntPoint() {
		// default constructor
	}

	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "IntPoint[" + this.x + ", " + this.y + "]";
	}

	public void setLocation(IntPoint p) {
		setLocation(p.getX(), p.getY());
	}

	public static double distanceSq(int x1, int y1, int x2, int y2) {
		x1 -= x2;
		y1 -= y2;
		return (x1 * x1 + y1 * y1);
	}

	public static double distance(int x1, int y1, int x2, int y2) {
		x1 -= x2;
		y1 -= y2;
		return Math.sqrt(x1 * x1 + y1 * y1);
	}

	public double distanceSq(int px, int py) {
		px -= getX();
		py -= getY();
		return (px * px + py * py);
	}

	public double distanceSq(IntPoint pt) {
		int px = pt.getX() - this.getX();
		int py = pt.getY() - this.getY();
		return (px * px + py * py);
	}

	public double distance(int px, int py) {
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
	}

	public double distance(IntPoint pt) {
		int px = pt.getX() - this.getX();
		int py = pt.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof IntPoint) {
			IntPoint p2d = (IntPoint) obj;
			return (getX() == p2d.getX()) && (getY() == p2d.getY());
		}
		return super.equals(obj);
	}

	public int hashCode() {
		HashCodeGenerator codeGenerator = new HashCodeGenerator();
		codeGenerator.addInt(this.x);
		codeGenerator.addInt(this.y);
		return codeGenerator.getResult();
	}
}
