package com.syrus.AMFICOM.Client.General.Command.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainWrapper;
import com.syrus.AMFICOM.client_.general.ui_.ObjComboBox;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;


public class SessionDomainCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionDomainCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SessionDomainCommand(dispatcher, aContext);
	}

	public void execute()
	{
		Set availableDomains;
		try {
			availableDomains = LoginManager.getAvailableDomains();
			if (availableDomains == null) {
				/* TODO infrom user ? */
				return;
			}				
		} catch (CommunicationException e) {
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Нет сессии!"));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModel.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
				JOptionPane.ERROR_MESSAGE, null);
			return;
		} catch (LoginException e) {
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Нет сессии!"));
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel.getString("errorWrongLogin"),
				LangModel.getString("errorTitleOpenSession"), JOptionPane.ERROR_MESSAGE, null);
			return;
		}		

		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Выбор домена..."));
		
		/* TODO may be ObjComboBox for Collection as parameter */
		ObjComboBox objComboBox = new ObjComboBox(DomainWrapper.getInstance(),new ArrayList(availableDomains), StorableObjectWrapper.COLUMN_NAME);
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
		

		int result = JOptionPane.showOptionDialog(Environment.getActiveWindow(), objComboBox, LangModel
				.getString("SessionDomainTitle"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
			new Object[] { LangModel.getString("buttonSelect"), LangModel.getString("buttonCancel")}, LangModel
					.getString("buttonSelect"));
		if (result == JOptionPane.OK_OPTION) {
			Domain selectedDomain = (Domain) objComboBox.getSelectedItem();
			Identifier domainId = selectedDomain.getId();
			try {
				LoginManager.selectDomain(domainId);
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый домен выбран"));
				dispatcher.notify(new ContextChangeEvent(domainId, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
			} catch (CommunicationException e) {
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Ошибка соединения!"));
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModel
						.getString("Error server connection"), LangModel.getString("errorTitleOpenSession"),
					JOptionPane.ERROR_MESSAGE, null);
				return;
			}
		} else {
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModel.getString("Aborted")));
		}

//		SessionDomainDialog sDialog = new SessionDomainDialog(aContext.getSessionInterface().getUserId());
//
//		sDialog.setDomain(aContext.getSessionInterface().getDomainId());
//
//		sDialog.setModal(true);
//
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension frameSize = sDialog.getSize();
//		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//
//		sDialog.show();
//
//		if(sDialog.retCode == sDialog.RET_CANCEL)
//		{
//			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModel.getString("Aborted")));
//			return;
//		}
//
//		if(sDialog.retCode == sDialog.RET_OK)
//		{
//			try
//			{
////				RISDSessionInfo rsi = (RISDSessionInfo )aContext.getSessionInterface();
////				rsi.accessIdentity.domain_id = sDialog.domain_id;
//				aContext.getSessionInterface().setDomainId(sDialog.domain_id);
//			}
//			catch(Exception ex)
//			{
//				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Ошибка соединения!"));
//				System.out.println("DOMAIN not set - not a RISD connection!");
//			}
//			
//			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый домен выбран"));
//			dispatcher.notify(new ContextChangeEvent(
//					sDialog.domain_id,
//					ContextChangeEvent.DOMAIN_SELECTED_EVENT));
//		}
	}
}
