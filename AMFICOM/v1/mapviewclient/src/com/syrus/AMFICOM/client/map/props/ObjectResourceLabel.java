/*-
 * $$Id: ObjectResourceLabel.java,v 1.7 2005/09/30 16:08:40 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import javax.swing.JLabel;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/30 16:08:40 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ObjectResourceLabel extends JLabel implements Comparable {
	private Object or;

	public ObjectResourceLabel(Object or, String text) {
		super(text);
		this.or = or;
		setOpaque(true);
	}

	public int compareTo(Object o) {
		int result = 0;
		if(o instanceof ObjectResourceLabel) {
			ObjectResourceLabel orl = (ObjectResourceLabel )o;
			result = this.getText().compareTo(orl.getText());
		}
		return result;
	}

	public Object getOR() {
		return this.or;
	}
}
