/*-
* $Id: PopupNotificationCellRenderer.java,v 1.1 2005/11/22 11:26:46 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/22 11:26:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
final class PopupNotificationCellRenderer extends DefaultListCellRenderer {
	
	private static final double FACTOR = 0.9;
	
	private Map<Color, Color> zebraColorMap;
	
	public PopupNotificationCellRenderer() {
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
		
		// only messages as value available
		final PopupNotificationEvent message = (PopupNotificationEvent) value;
		label.setIcon(this.getIcon(message));
		label.setText(CommonUIUtilities.convertToHTMLString(message.getMessage()));
		
		return label;
	}
	
	private final Color getDarkerColor(final Color color) {
		return new Color(
			Math.max((int) (color.getRed() * FACTOR), 0), 
			Math.max((int) (color.getGreen() * FACTOR), 0), 
			Math.max((int) (color.getBlue() * FACTOR), 0));
	}
	
	private final Icon getIcon(final PopupNotificationEvent message) {
		// use OptionPane icons
		/**
		 * @see javax.swing.plaf.basic.BasicOptionPaneUI#getIconForType(int)
		 */
		final Severity severity = message.getSeverity();
		return UIManager.getIcon(
			severity == Severity.SEVERITY_HARD ? 
				"OptionPane.errorIcon" : 
					severity == Severity.SEVERITY_SOFT ?  
					"OptionPane.warningIcon" :
						"OptionPane.informationIcon");

	}
}
