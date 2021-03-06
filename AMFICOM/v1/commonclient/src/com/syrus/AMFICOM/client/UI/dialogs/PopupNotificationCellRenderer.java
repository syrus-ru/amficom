/*-
* $Id: PopupNotificationCellRenderer.java,v 1.6 2006/07/02 16:18:10 bass Exp $
*
* Copyright ? 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import static java.util.logging.Level.SEVERE;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.ZebraListCellRenderer;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2006/07/02 16:18:10 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
final class PopupNotificationCellRenderer extends ZebraListCellRenderer {
	private static final long serialVersionUID = 4270224926345083694L;

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
		
		// only popupNotificationEvents as value available
		final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) value;
		/*
		 * In case of exception during object retrieval, message will
		 * be set to exception's message, and severity will be set to
		 * NONE in order to differ from all valid messages (since their
		 * severity can be either SOFT or HARD).
		 */
		String richTextMessage;
		Severity severity;
		try {
			final LineMismatchEvent lineMismatchEvent =
					(LineMismatchEvent) StorableObjectPool.getStorableObject(
							popupNotificationEvent.getLineMismatchEventId(),
							true);
			richTextMessage = "<html>" + lineMismatchEvent.getRichTextMessage() + "</html>";
			try {
				final ReflectogramMismatchEvent reflectogramMismatchEvent = lineMismatchEvent.getReflectogramMismatchEvent();
				severity = reflectogramMismatchEvent.getSeverity();
			} catch (final ApplicationException ae) {
				Log.debugMessage(ae, SEVERE);
				severity = Severity.SEVERITY_NONE;
			}
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			richTextMessage = CommonUIUtilities.convertToHTMLString(ae.getMessage());
			severity = Severity.SEVERITY_NONE;
		}
		
		label.setIcon(this.getIcon(severity));
		label.setText(richTextMessage);
		
		return label;
	}	
	
	private final Icon getIcon(final Severity severity) {
		// use OptionPane icons
		/**
		 * @see javax.swing.plaf.basic.BasicOptionPaneUI#getIconForType(int)
		 */
		return UIManager.getIcon(
			severity == Severity.SEVERITY_HARD
				? "OptionPane.errorIcon"
				: severity == Severity.SEVERITY_SOFT
						? "OptionPane.warningIcon"
						: "OptionPane.informationIcon");

	}
}
