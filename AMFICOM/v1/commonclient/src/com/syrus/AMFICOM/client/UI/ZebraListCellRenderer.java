/*-
* $Id: ZebraListCellRenderer.java,v 1.1 2006/02/02 08:32:22 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;


/**
 * Zebra list cell renderer
 * 
 * @version $Revision: 1.1 $, $Date: 2006/02/02 08:32:22 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class ZebraListCellRenderer extends DefaultListCellRenderer {
	
	private static final double FACTOR = 0.9;
	
	private Map<Color, Color> zebraColorMap;
	
	public ZebraListCellRenderer() {
		this.zebraColorMap = new HashMap<Color, Color>();
	}
	
	@Override
	public Component getListCellRendererComponent(final JList list,
			final Object value,
			final int index,
			final boolean isSelected,
			final boolean cellHasFocus) {
		
		// warning ! based on the fact that DefaultListCellRenderer extends JLabel
		final JLabel label = 
			(JLabel) super.getListCellRendererComponent(list, 
				value, 
				index, 
				isSelected,
				cellHasFocus);
		
		// rotate element background color
		if (index % 2 == 1) {
			final Color background = label.getBackground();
			Color darkColor = this.zebraColorMap.get(background);
			if (darkColor == null) {
				darkColor = this.getDarkerColor(background);
				this.zebraColorMap.put(background, darkColor);
			}
			label.setBackground(darkColor);
		}
		
		return label;
	}
	
	private final Color getDarkerColor(final Color color) {
		return new Color(
			Math.max((int) (color.getRed() * FACTOR), 0), 
			Math.max((int) (color.getGreen() * FACTOR), 0), 
			Math.max((int) (color.getBlue() * FACTOR), 0));
	}

}
