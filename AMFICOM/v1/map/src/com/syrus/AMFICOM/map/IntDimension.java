/*
* $Id: IntDimension.java,v 1.8 2005/07/17 05:20:43 arseniy Exp $
*
* Copyright � 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.map;


/**
 * ���� ����� �����, ��������������� ����������� ����-���� � ����������
 * ������������ (������ � ������)
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/07/17 05:20:43 $
 * @module map_v1
 */

public class IntDimension implements java.io.Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256440309234872368L;

	/**
	 * ������.
	 */
	private int	width;
	/**
	 * ������.
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntDimension) {
			IntDimension d = (IntDimension) obj;
			return (this.width == d.width) && (this.height == d.height);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int sum = this.width + this.height;
		return sum * (sum + 1) / 2 + this.width;
	}

	@Override
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
