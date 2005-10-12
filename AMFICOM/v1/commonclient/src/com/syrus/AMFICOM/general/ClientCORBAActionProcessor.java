/*-
 * $Id: ClientCORBAActionProcessor.java,v 1.2 2005/10/12 06:21:11 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/10/12 06:21:11 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public class ClientCORBAActionProcessor extends DefaultCORBAActionProcessor {

	public ClientCORBAActionProcessor(final LoginRestorer loginRestorer) {
		super(loginRestorer);
	}

	@Override
	public void performAction(final CORBAAction action) throws ApplicationException {
		while (true) {
			try {
				try {
					action.perform();
					return;
				} catch (final AMFICOMRemoteException are) {
					switch (are.errorCode.value()) {
						case IdlErrorCode._ERROR_NOT_LOGGED_IN:
							if (super.restoreLogin()) {
								continue;
							}
							Log.debugMessage("ClientCORBAActionProcessor.performAction | Login not restored", Level.INFO);
							throw new LoginException(are.message);
						default:
							throw new RetrieveObjectException(are.message);
					}
				}
			} catch (final ApplicationException ae) {
				final String retry = I18N.getString("ClientCORBAActionProcessor.retryButtonText");

				final JOptionPane pane = new JOptionPane(I18N.getString("ClientCORBAActionProcessor.connectionProblemText"),
						JOptionPane.ERROR_MESSAGE,
						JOptionPane.OK_CANCEL_OPTION,
						null,
						new Object[] { retry, I18N.getString("ClientCORBAActionProcessor.cancelButtonText") },
						null);

				pane.setValue(retry);

				final JDialog dialog = pane.createDialog(null, I18N.getString("Error"));

				dialog.setVisible(true);

				final Object object = pane.getValue();
				if (object != retry) {
					throw ae;
				}
			}
		}
	}

}
