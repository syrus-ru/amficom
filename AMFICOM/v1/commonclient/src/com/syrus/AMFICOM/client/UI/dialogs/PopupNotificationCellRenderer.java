/*-
* $Id: PopupNotificationCellRenderer.java,v 1.2 2006/02/02 08:32:46 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.ZebraListCellRenderer;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;


/**
 * @version $Revision: 1.2 $, $Date: 2006/02/02 08:32:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
final class PopupNotificationCellRenderer extends ZebraListCellRenderer {
	
	@Override
	public final Component getListCellRendererComponent(final JList list,
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
		
		// only messages as value available
		final PopupNotificationEvent message = (PopupNotificationEvent) value;
		label.setIcon(this.getIcon(message));
		label.setText(CommonUIUtilities.convertToHTMLString(message.getMessage()));
		
		return label;
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
