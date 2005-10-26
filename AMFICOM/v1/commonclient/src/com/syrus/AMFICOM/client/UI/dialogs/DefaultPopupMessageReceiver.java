/*-
* $Id: DefaultPopupMessageReceiver.java,v 1.1 2005/10/26 13:07:00 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.UI.dialogs;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.event.PopupMessageReceiver;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/26 13:07:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class DefaultPopupMessageReceiver implements PopupMessageReceiver {

	public void receiveMessage(final Event event) {
		if (event instanceof PopupNotificationEvent) {
			final PopupNotificationEvent popupNotificationEvent = 
				(PopupNotificationEvent) event;
			JOptionPane.showMessageDialog(null, 
				popupNotificationEvent.getMessage(),
				I18N.getString("Common.ClientServantManager.NotificationMessage"),
				JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
}

