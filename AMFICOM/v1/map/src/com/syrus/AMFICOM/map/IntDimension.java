/*
* $Id: IntDimension.java,v 1.6 2005/01/27 14:43:37 krupenn Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;


/**
 * Пара целых чисел, соответствующих размерности чего-либо в двухмерном
 * пространстве (ширина и высота)
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/01/27 14:43:37 $
 * @module map_v1
 */

public class IntDimension implements java.io.Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256440309234872368L;

	/**
	 * Ширина.
	 */
	private int	width;
	/**
	 * Высота.
	 */
	private int	height;

	public IntDimension() {
		this(0, 0);
	}

	public IntDimension(IntDimension d) {
		this(d.width, d.height);
	}

	public IntDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setSize(double width, double height) {
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
	}

	public IntDimension getSize() {
		return new IntDimension(this.width, this.height);
	}

	public void setSize(IntDimension d) {
		setSize(d.width, d.height);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean equals(Object obj) {
		if (obj instanceof IntDimension) {
			IntDimension d = (IntDimension) obj;
			return (this.width == d.width) && (this.height == d.height);
		}
		return false;
	}

	public int hashCode() {
		int sum = this.width + this.height;
		return sum * (sum + 1) / 2 + this.width;
	}

	public String toString() {
		return getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
