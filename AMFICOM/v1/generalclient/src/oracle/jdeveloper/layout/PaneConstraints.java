/*
 * $Id: PaneConstraints.java,v 1.2 2005/05/18 14:01:18 bass Exp $
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
public class PaneConstraints implements Serializable {

	public static final String BOTTOM = "Bottom";

	public static final String LEFT = "Left";

	public static final String RIGHT = "Right";

	public static final String ROOT = "Root";

	public static final String TOP = "Top";

	private static final long serialVersionUID = 3257850991125674292L;

	public String name;

	public String position;

	public float proportion;

	public String splitComponentName;

	public PaneConstraints() {
		this.proportion = 0.5F;
		this.position = "Top";
	}

	public PaneConstraints(String s, String s1, String s2, float f) {
		this.proportion = 0.5F;
		this.position = "Top";
		this.name = s;
		this.splitComponentName = s1;
		this.position = s2;
		this.proportion = f;
	}

	public Object clone() {
		return new PaneConstraints(this.name, this.splitComponentName,
				this.position, this.proportion);
	}

	public String toString() {
		return this.name
				+ ": " + this.splitComponentName + "," + this.position
				+ " proportion:" + this.proportion;
	}
}
