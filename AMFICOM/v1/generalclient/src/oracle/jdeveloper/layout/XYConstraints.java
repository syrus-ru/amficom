/*
 * $Id: XYConstraints.java,v 1.2 2005/05/18 14:01:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.io.Serializable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class XYConstraints implements Cloneable, Serializable {
	private static final long serialVersionUID = 3544675079020556856L;

	int height;

	int width;

	int x;

	int y;

	public XYConstraints() {
		this(0, 0, 0, 0);
	}

	public XYConstraints(int i, int j, int k, int l) {
		this.x = i;
		this.y = j;
		this.width = k;
		this.height = l;
	}

	public Object clone() {
		return new XYConstraints(this.x, this.y, this.width,
				this.height);
	}

	public boolean equals(Object obj) {
		if (obj instanceof XYConstraints) {
			XYConstraints xyconstraints = (XYConstraints) obj;
			return xyconstraints.x == this.x
					&& xyconstraints.y == this.y
					&& xyconstraints.width == this.width
					&& xyconstraints.height == this.height;
		}
		return false;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int hashCode() {
		return this.x ^ this.y * 37 ^ this.width * 43 ^ this.height
				* 47;
	}

	public void setHeight(int i) {
		this.height = i;
	}

	public void setWidth(int i) {
		this.width = i;
	}

	public void setX(int i) {
		this.x = i;
	}

	public void setY(int i) {
		this.y = i;
	}

	public String toString() {
		return "XYConstraints[" + this.x + "," + this.y + "," + this.width + ","
				+ this.height + "]";
	}
}
