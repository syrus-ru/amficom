/*
 * $Id: GridBagConstraints2.java,v 1.2 2005/05/18 14:01:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package oracle.jdeveloper.layout;

import java.awt.*;
import java.io.Serializable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 14:01:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 * @deprecated
 */
public class GridBagConstraints2 extends GridBagConstraints implements
		Serializable {
	private static final long serialVersionUID = 3690760613475266865L;

	public GridBagConstraints2(int i, int j, int k, int l, double d,
			double d1, int i1, int j1, Insets insets, int k1, int l1) {
		this.gridx = i;
		this.gridy = j;
		this.gridwidth = k;
		this.gridheight = l;
		this.fill = j1;
		this.ipadx = k1;
		this.ipady = l1;
		this.insets = insets;
		this.anchor = i1;
		this.weightx = d;
		this.weighty = d1;
	}

	public String toString() {
		return ": "	+ this.gridx + "," + this.gridy + "," + this.gridwidth + ","
				+ this.gridheight;
	}
}
