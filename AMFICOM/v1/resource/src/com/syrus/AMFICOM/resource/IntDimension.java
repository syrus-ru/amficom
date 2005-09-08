/*
* $Id: IntDimension.java,v 1.2 2005/09/08 09:07:29 max Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.io.Serializable;


/**
 * Пара целых чисел, соответствующих размерности чего-либо в двухмерном
 * пространстве (ширина и высота)
 *
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/09/08 09:07:29 $
 * @module resource
 */

public class IntDimension implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256440309234872368L;

	/**
	 * Ширина.
	 */
	private int width;
	/**
	 * Высота.
	 */
	private int height;

	public IntDimension() {
		this(0, 0);
	}

	public IntDimension(final IntDimension d) {
		this(d.width, d.height);
	}

	public IntDimension(final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setSize(final double width, final double height) {
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
	}

	public IntDimension getSize() {
		return new IntDimension(this.width, this.height);
	}

	public void setSize(final IntDimension d) {
		setSize(d.width, d.height);
	}

	public void setSize(final int width, final int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof IntDimension) {
			final IntDimension d = (IntDimension) obj;
			return (this.width == d.width) && (this.height == d.height);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int sum = this.width + this.height;
		return sum * (sum + 1) / 2 + this.width;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public void setWidth(final int width) {
		this.width = width;
	}
}
