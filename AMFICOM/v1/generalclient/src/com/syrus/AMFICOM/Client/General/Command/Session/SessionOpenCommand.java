/*
 * $Id: SessionOpenCommand.java,v 1.9 2004/09/27 12:08:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportBuilder;
import com.syrus.AMFICOM.Client.General.UI.Session.SessionOpenDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import java.awt.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2004/09/27 12:08:55 $
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

	public void execute()
	{
		ConnectionInterface connection = ConnectionInterface.getInstance();
		if(!connection.isConnected()) {
			new CheckConnectionCommand(dispatcher, aContext).execute();
			if (!connection.isConnected())
			{
				dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_FAILED_EVENT));
				return;
			}
		}
		final SessionOpenDialog sDialog = new SessionOpenDialog();

		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Открытие сессии..."));
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

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Инициализация начальных данных"));

			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, true));

      ReportBuilder.invokeAsynchronously(new Runnable()
      {
        public void run()
        {
          dataSource.LoadUserDescriptors();
          dataSource.LoadExecs();
          dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_PROGRESS_BAR, false));

          SessionInterface sess = dataSource.getSession();
          
          // Берем сохраненный локально с прошлой сессии домен
          String evDomainId = com.syrus.AMFICOM.Client.General.Model.Environment.getDomainId();
          
          // Проверяем, может ли текущий пользователь с ним работать
          if(Checker.checkObject(sess.getUserId(), Domain.typ, evDomainId, Checker.read))
            sess.setDomainId(evDomainId);
          else
            sess.setDomainId("");
    
          dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Сессия открыта"));
          dispatcher.notify(new ContextChangeEvent(
              sDialog.si,
              ContextChangeEvent.SESSION_OPENED_EVENT));
        }
      },
      "Идёт загрузка. Пожалуйста, подождите.");
		}
	}
}
