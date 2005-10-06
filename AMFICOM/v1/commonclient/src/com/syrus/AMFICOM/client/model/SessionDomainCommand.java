
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
import com.syrus.AMFICOM.client.resource.I18N;
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

	public SessionDomainCommand(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void setParameter(final String field, final Object value) {
		if (field.equals("dispatcher")) {
			this.setDispatcher((Dispatcher) value);
		}
	}

	public void setDispatcher(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void execute() {
		Set<Domain> availableDomains;
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
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString("Common.StatusBar.NoSession")));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGeneral.getString("Error.ServerConnection"),
					LangModelGeneral.getString("Error.ErrorOccur"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		} catch (LoginException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString("Common.StatusBar.NoSession")));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGeneral.getString("Error.WrongLogin"),
					LangModelGeneral.getString("Error.ErrorOccur"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
				StatusMessageEvent.STATUS_MESSAGE,
				I18N.getString("Common.StatusBar.DomainSelection")));

		final WrapperedComboBox objComboBox = new WrapperedComboBox<Domain>(DomainWrapper.getInstance(),
				new ArrayList<Domain>(availableDomains),
				StorableObjectWrapper.COLUMN_NAME,
				StorableObjectWrapper.COLUMN_ID);
		{
			final Identifier domainId = Environment.getDomainId();
			if (domainId != null) {
				try {
					objComboBox.setSelectedItem(StorableObjectPool.getStorableObject(domainId, true));
				} catch (ApplicationException e) {
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelGeneral.getString("Error.ServerConnection"),
							LangModelGeneral.getString("Error.ErrorOccur"),
							JOptionPane.ERROR_MESSAGE,
							null);
				}
			}
		}

		final int result1 = JOptionPane.showOptionDialog(Environment.getActiveWindow(),
				objComboBox,
				LangModelGeneral.getString("SelectDomain.Title"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				new Object[] { LangModelGeneral.getString("Button.Select"), LangModelGeneral.getString("Button.Cancel") },
				LangModelGeneral.getString("Button.Select"));
		if (result1 == JOptionPane.OK_OPTION) {
			final Domain selectedDomain = (Domain) objComboBox.getSelectedItem();
			if (selectedDomain != null) {
				final Identifier domainId = selectedDomain.getId();
				try {
					LoginManager.selectDomain(domainId);
					this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Common.StatusBar.NewDomainSelected")));
					this.dispatcher.firePropertyChange(new ContextChangeEvent(domainId, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
				} catch (CommunicationException e) {
					this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
							StatusMessageEvent.STATUS_MESSAGE,
							I18N.getString("Common.StatusBar.ConnectionError")));
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelGeneral.getString("Error.ServerConnection"),
							LangModelGeneral.getString("Error.ErrorOccur"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}
			}
		} else {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString("Common.StatusBar.Aborted")));
		}
	}
}
