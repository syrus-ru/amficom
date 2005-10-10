/*-
 * $Id: BaseSessionEnvironment.java,v 1.22 2005/10/10 11:42:28 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2005/10/10 11:42:28 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class BaseSessionEnvironment {
	protected BaseConnectionManager baseConnectionManager;
	protected PoolContext poolContext;
	protected Date sessionEstablishDate;
	protected boolean sessionEstablished;

	protected class LogoutShutdownHook extends Thread {

		@Override
		public void run() {
			try {
				BaseSessionEnvironment.this.logout0();
			}
			catch (final ApplicationException ae) {
				Log.errorException(ae);
			}
		}

	}

	protected LogoutShutdownHook logoutShutdownHook;

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final LoginRestorer loginRestorer) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;

		LoginManager.init(this.baseConnectionManager, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager);		
		this.poolContext.init();

		this.logoutShutdownHook = new LogoutShutdownHook();
		
		final CORBAActionProcessor actionProcessor = this.createCORBAActionProcessor();
		CORBAObjectLoader.setActionProcessor(actionProcessor);
		IdentifierPool.setActionProcessor(actionProcessor);

		this.sessionEstablishDate = null;
		this.sessionEstablished = false;
		
	}
	
	private CORBAActionProcessor createCORBAActionProcessor() {
		return new CORBAActionProcessor() {

			public void performAction(final CORBAAction action) throws ApplicationException {
				while (true) {
					try {
						try {
							action.performAction();
							return;
						} catch (final AMFICOMRemoteException are) {
							switch (are.errorCode.value()) {
								case IdlErrorCode._ERROR_NOT_LOGGED_IN:								
									if (LoginManager.restoreLogin()) {
										continue;
									}
									Log.debugMessage("BaseSessionEnvironment.performAction | Login not restored", Level.INFO);
									throw new LoginException(are.message);						
								default:
									throw new RetrieveObjectException(are.message);
							}
						}
					} catch(final ApplicationException ae) {
						final String retry = I18N.getString("BaseSessionEnvironment.retryButtonText");
						
						final JOptionPane pane = new JOptionPane(I18N.getString("BaseSessionEnvironment.connectionProblemText"), 
							JOptionPane.ERROR_MESSAGE, 
							JOptionPane.OK_CANCEL_OPTION,
							null, 
							new Object[] { 
								retry, 
								I18N.getString("BaseSessionEnvironment.cancelButtonText")}, 
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
		};
	}
	
	public BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}
//
//	public PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public Date getSessionEstablishDate() {
		return this.sessionEstablishDate;
	}

	public boolean sessionEstablished() {
		return this.sessionEstablished;
	}

	public final void login(final String login, final String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		IdentifierPool.deserialize();
		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUSaver());
		} catch (final CommunicationException ce) {
			StorableObjectPool.clean();
			throw ce;
		} catch (final ApplicationException e) {
			StorableObjectPool.clean();
			throw new LoginException(I18N.getString("Error.Text.DeserializationFailed"));
		}

		this.baseConnectionManager.getCORBAServer().addShutdownHook(this.logoutShutdownHook);

		this.sessionEstablishDate = new Date(System.currentTimeMillis());
		this.sessionEstablished = true;
	}

	public final void logout() throws CommunicationException, LoginException {
		this.baseConnectionManager.getCORBAServer().removeShutdownHook(this.logoutShutdownHook);

		this.logout0();

		//@todo Maybe move to logout0()
		this.sessionEstablishDate = null;
		this.sessionEstablished = false;
	}

	protected void logout0() throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUSaver());
		LoginManager.logout();
	}
}
