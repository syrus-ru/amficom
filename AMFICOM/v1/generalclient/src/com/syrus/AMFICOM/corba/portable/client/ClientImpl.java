/*
 * $Id: ClientImpl.java,v 1.4 2004/08/06 06:14:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.client;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Object.ui.JAlertingMessageDialog;
import com.syrus.AMFICOM.corba.portable.alarm.Message;
import com.syrus.AMFICOM.corba.portable.common.MessageDeliveryFailedException;
import java.util.LinkedList;
import org.omg.CORBA.LongHolder;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/06 06:14:09 $
 * @author $Author: bass $
 * @module general_v1
 */
public class ClientImpl implements ClientOperations {
	volatile JAlertingMessageDialog jAlertingMessageDialog;

	volatile boolean dialogVisible = false;

	public void reportPopupMessages(Message[] messageSeq)
			throws MessageDeliveryFailedException {
		try {
			try {
				LinkedList linkedList = new LinkedList();
				for (int i = 0; i < messageSeq.length; i ++)
					linkedList.add(messageSeq[i]);
				/*
				 * The dialog can be used multiple times, new copies are
				 * created only because dialog's parent may change.
				 */
				if ((this.jAlertingMessageDialog == null) || (!this.dialogVisible)) {
					this.jAlertingMessageDialog = new JAlertingMessageDialog(
						Environment.getActiveWindow(), true);
					this.jAlertingMessageDialog.setMessageSeq(linkedList);
					new Thread() {
						public void run() {
							/*
							 * It may take a certain amount of time for the
							 * dialog to become visible. We can't make any
							 * difference whether it's just invisible or
							 * "invisible but going to become visible", that's
							 * why isVisible() is unusable here.
							 *
							 * Since the dialog is modal, show() blocks for all
							 * the time the dialog is visible.
							 */
							ClientImpl.this.dialogVisible = true;
							ClientImpl.this.jAlertingMessageDialog.show();
							ClientImpl.this.dialogVisible = false;
						}
					}.start();
				} else
					this.jAlertingMessageDialog.appendMessageSeq(linkedList);
			} catch (NullPointerException npe) {
				throw new IllegalArgumentException(
					"Message sequence cannot be null.");
			}
		} catch (IllegalArgumentException iae) {
			MessageDeliveryFailedException mdfe
				= new MessageDeliveryFailedException(iae.getLocalizedMessage());
			mdfe.initCause(iae);
			mdfe.setStackTrace(iae.getStackTrace());
			throw mdfe;
		}
	}

	public void syncPing(LongHolder clientTimeMillis) {
		clientTimeMillis.value = System.currentTimeMillis();
	}
}
