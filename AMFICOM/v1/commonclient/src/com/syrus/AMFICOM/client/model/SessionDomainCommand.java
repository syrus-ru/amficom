
package com.syrus.AMFICOM.client.model;

import java.util.ArrayList;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainWrapper;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class SessionDomainCommand extends AbstractCommand {

	private Dispatcher	dispatcher;

	/**
	 * @deprecated use {@link #SessionDomainCommand(Dispatcher)}
	 */
	public SessionDomainCommand(Dispatcher dispatcher, ApplicationContext aContext) {
		this(dispatcher);
	}

	public SessionDomainCommand(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher")) {
			setDispatcher((Dispatcher) value);
		}
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void execute() {
		Set availableDomains;
		try {
			availableDomains = LoginManager.getAvailableDomains();
			if (availableDomains == null || availableDomains.isEmpty()) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelGeneral.getString("Error.NoDomains"),
						LangModelGeneral.getString("Error.ErrorOccur"), 
						JOptionPane.ERROR_MESSAGE, 
						null);
				return;
			}
		} catch (CommunicationException e) {
			this.dispatcher.firePropertyChange(
				new StatusMessageEvent(this, 
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("StatusBar.NoSession")));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGeneral.getString("Error.ServerConnection"), 
					LangModelGeneral.getString("Error.ErrorOccur"),
					JOptionPane.ERROR_MESSAGE, 
					null);
			return;
		} catch (LoginException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, 
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelGeneral.getString("StatusBar.NoSession")));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					LangModelGeneral.getString("Error.WrongLogin"),
					LangModelGeneral.getString("Error.ErrorOccur"), 
					JOptionPane.ERROR_MESSAGE, 
					null);
			return;
		}

		this.dispatcher.firePropertyChange(
			new StatusMessageEvent(this, 
					StatusMessageEvent.STATUS_MESSAGE,
					LangModelGeneral.getString("StatusBar.DomainSelection")));

		WrapperedComboBox objComboBox = 
			new WrapperedComboBox(DomainWrapper.getInstance(), 
					new ArrayList(availableDomains),
					StorableObjectWrapper.COLUMN_NAME, 
					StorableObjectWrapper.COLUMN_ID);
		{
			Identifier domainId = Environment.getDomainId();
			if (domainId != null) {
				try {
					objComboBox.setSelectedItem(
						StorableObjectPool.getStorableObject(domainId, true));
				} catch (ApplicationException e) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
						LangModelGeneral.getString("Error.ServerConnection"), 
						LangModelGeneral.getString("Error.ErrorOccur"),
						JOptionPane.ERROR_MESSAGE, 
						null);
				}
			}
		}

		int result1 = JOptionPane.showOptionDialog(Environment.getActiveWindow(), 
					objComboBox, 
					LangModelGeneral.getString("SelectDomain.Title"), 
					JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.PLAIN_MESSAGE, 
					null,
					new Object[] { 
						LangModelGeneral.getString("Button.Select"), 
						LangModelGeneral.getString("Button.Cancel")}, 
					LangModelGeneral.getString("Button.Select"));
		if (result1 == JOptionPane.OK_OPTION) {
			Domain selectedDomain = (Domain) objComboBox.getSelectedItem();
			if (selectedDomain != null) {
				Identifier domainId = selectedDomain.getId();
				try {
					LoginManager.selectDomain(domainId);
					this.dispatcher.firePropertyChange(
						new StatusMessageEvent(this, 
								StatusMessageEvent.STATUS_MESSAGE,
								LangModelGeneral.getString("StatusBar.NewDomainSelected")));
					this.dispatcher.firePropertyChange(
						new ContextChangeEvent(domainId,
								ContextChangeEvent.DOMAIN_SELECTED_EVENT));
				} catch (CommunicationException e) {
					this.dispatcher.firePropertyChange(
						new StatusMessageEvent(this, 
							StatusMessageEvent.STATUS_MESSAGE,
							LangModelGeneral.getString("StatusBar.ConnectionError")));
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
							LangModelGeneral.getString("Error.ServerConnection"), 
							LangModelGeneral.getString("Error.ErrorOccur"),
							JOptionPane.ERROR_MESSAGE, 
							null);
					return;
				}
			}
		} else {
			this.dispatcher.firePropertyChange(
				new StatusMessageEvent(this, 
						StatusMessageEvent.STATUS_MESSAGE,
						LangModelGeneral.getString("StatusBar.Aborted")));
		}
	}
}
