
package com.syrus.AMFICOM.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainWrapper;
import com.syrus.AMFICOM.client.UI.WrapperedComboBox;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

public class SessionDomainCommand extends AbstractCommand {

	private Dispatcher	dispatcher;

	private ApplicationContext aContext;

	public SessionDomainCommand(Dispatcher dispatcher, ApplicationContext aContext) {
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher")) {
			setDispatcher((Dispatcher) value);
		}
		// else if (field.equals("aContext"))
		// setApplicationContext((ApplicationContext) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	// public void setApplicationContext(ApplicationContext aContext) {
	// this.aContext = aContext;
	// }

	public void execute() {
		Set availableDomains;
		try {
			availableDomains = LoginManager.getAvailableDomains();
			if (availableDomains == null) {
				/* TODO infrom user ? */
				return;
			}
		} catch (CommunicationException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																		"Нет сессии!"));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModel.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
				JOptionPane.ERROR_MESSAGE, null);
			return;
		} catch (LoginException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																		"Нет сессии!"));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel.getString("errorWrongLogin"),
				LangModel.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);
			return;
		}

		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																	"Выбор домена..."));

		/* TODO may be ObjComboBox for Collection as parameter */
		WrapperedComboBox objComboBox = new WrapperedComboBox(DomainWrapper.getInstance(), new ArrayList(availableDomains),
													StorableObjectWrapper.COLUMN_NAME, StorableObjectWrapper.COLUMN_ID);
		{
			Identifier domainId = Environment.getDomainId();
			for (Iterator iterator = availableDomains.iterator(); iterator.hasNext();) {
				Domain domain = (Domain) iterator.next();
				if (domain.getId().equals(domainId)) {
					objComboBox.setSelectedItem(domain);
					break;
				}
			}
		}

		int result1 = JOptionPane.showOptionDialog(Environment.getActiveWindow(), objComboBox, LangModel
				.getString("SessionDomainTitle"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
			new Object[] { LangModel.getString("buttonSelect"), LangModel.getString("buttonCancel")}, LangModel
					.getString("buttonSelect"));
		if (result1 == JOptionPane.OK_OPTION) {
			Domain selectedDomain = (Domain) objComboBox.getSelectedItem();
			if (selectedDomain != null) {
				Identifier domainId = selectedDomain.getId();
				try {
					LoginManager.selectDomain(domainId);
					this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																				"Новый домен выбран"));
					this.dispatcher
							.firePropertyChange(new ContextChangeEvent(domainId,
																		ContextChangeEvent.DOMAIN_SELECTED_EVENT));
				} catch (CommunicationException e) {
					this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																				"Ошибка соединения!"));
					JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
							.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
						JOptionPane.ERROR_MESSAGE, null);
					return;
				}
			}
		} else {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE,
																		LangModel.getString("Aborted")));
		}
	}
}
