/*
 * $Id: SessionOpenCommand.java,v 1.11 2005/02/10 13:17:07 stas Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.General.UI.Session.SessionOpenDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import java.awt.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.11 $, $Date: 2005/02/10 13:17:07 $
 * @module generalclient_v1
 */
public class SessionOpenCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionOpenCommand()
	{
	}

	public SessionOpenCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionOpenCommand(dispatcher, aContext);
	}
	
	public void execute(){
		if(Environment.getConnectionType().equals(Environment.CONNECTION_EMPTY))
			executeLocal();
		else
			executeRemote();
	}
	
	private void executeLocal()
	{
		SessionInterface ssi = aContext.getSessionInterface().OpenSession();

		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "�������� ������..."));
		dispatcher.notify(new ContextChangeEvent(aContext.getSessionInterface(), ContextChangeEvent.SESSION_CHANGING_EVENT));

			final DataSourceInterface dataSource = aContext.getApplicationModel().getDataSource(aContext.getSessionInterface());
//
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "������������� ��������� ������"));

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, true));

      dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, false));

      SessionInterface sess = dataSource.getSession();
          
          dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE,
				"������ �������"));
		dispatcher.notify(new ContextChangeEvent(aContext.getSessionInterface(),
				ContextChangeEvent.SESSION_OPENED_EVENT));
	}
	
	private void executeRemote()
	{
		ConnectionInterface connection = ConnectionInterface.getInstance();
		if(!connection.isConnected())
		{
			new CheckConnectionCommand(dispatcher, aContext).execute();
			if (!connection.isConnected())
			{
				dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_FAILED_EVENT));
				return;
			}
		}
		final SessionOpenDialog sDialog = new SessionOpenDialog();

		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "�������� ������..."));
		dispatcher.notify(new ContextChangeEvent(aContext.getSessionInterface(), ContextChangeEvent.SESSION_CHANGING_EVENT));

		/**
		 * @todo SessionOpenDialog shouldn't hold any reference to
		 *       the connection instance.
		 */
		sDialog.ci = connection;
		sDialog.si = aContext.getSessionInterface();

		sDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = sDialog.getSize();
		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		sDialog.show();

		if(sDialog.retCode == sDialog.RET_CANCEL)
		{
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModel.getString("Aborted")));
			return;
		}

		if(sDialog.retCode == sDialog.RET_OK)
		{
			final DataSourceInterface dataSource = aContext.getApplicationModel().getDataSource(aContext.getSessionInterface());

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "������������� ��������� ������"));

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, true));

      ReportBuilder.invokeAsynchronously(new Runnable()
      {
        public void run()
        {
          dataSource.LoadUserDescriptors();
          dataSource.LoadExecs();
          dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, false));

          SessionInterface sess = dataSource.getSession();
          
          // ����� ����������� �������� � ������� ������ �����
          String evDomainId = com.syrus.AMFICOM.Client.General.Model.Environment.getDomainId();
          
          // ���������, ����� �� ������� ������������ � ��� ��������
          if(Checker.checkObject(sess.getUserId(), Domain.typ, evDomainId, Checker.read))
            sess.setDomainId(evDomainId);
          else
            sess.setDomainId("");
    
          dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "������ �������"));
          dispatcher.notify(new ContextChangeEvent(
          		aContext.getSessionInterface(),
              ContextChangeEvent.SESSION_OPENED_EVENT));
        }
      },
      "��� ��������. ����������, ���������.");
		}
	}
}
