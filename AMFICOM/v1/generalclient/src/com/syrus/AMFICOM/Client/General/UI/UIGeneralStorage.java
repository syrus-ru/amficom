/*
 * UIGeneralStorage.java
 * Created on 10.06.2004 13:55:54
 * 
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/08 07:58:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module generalclient_v1
 */
public final class UIGeneralStorage {

	public static final Font	DIALOG_FONT	= new Font("Dialog", Font.PLAIN, 11);
	public static final Font	SMALL_FONT	= new Font("Dialog", Font.PLAIN, 10);

	private UIGeneralStorage() {
		// singleton
		throw new Error("UIGeneralStorage is just a container for static methods and constans");
	}

	public static void fixHorizontalSize(JComponent component) {
		Dimension minimumSize = component.getMinimumSize();
		Dimension maximumSize = component.getMaximumSize();
		maximumSize.width = minimumSize.width;
		component.setMaximumSize(maximumSize);
		Dimension preferredSize = component.getPreferredSize();
		preferredSize.width = minimumSize.width;
		component.setPreferredSize(preferredSize);
	}
}
