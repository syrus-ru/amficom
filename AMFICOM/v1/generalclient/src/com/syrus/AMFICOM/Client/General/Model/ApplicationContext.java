/*
 * $Id: ApplicationContext.java,v 1.3 2004/09/27 10:00:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 10:00:46 $
 * @module generalclient_v1
 */
public final class ApplicationContext
{
	private SessionInterface session;
	
	private ApplicationModel applicationModel;
	
	private Dispatcher dispatcher;


	public void setSessionInterface(SessionInterface session)
	{
		this.session = session;
	}

	public void setApplicationModel(ApplicationModel applicationModel)
	{
		this.applicationModel = applicationModel;
	}


	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public SessionInterface getSessionInterface()
	{
		return session;
	}

	public ApplicationModel getApplicationModel()
	{
		return applicationModel;
	}

	public DataSourceInterface getDataSource()
	{
		return this.applicationModel.getDataSource(this.session);
	}

	/**
	 * @deprecated Use {@link ConnectionInterface#getInstance()} instead. 
	 */
	public ConnectionInterface getConnectionInterface()
	{
		return ConnectionInterface.getInstance();
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setConnectionInterface(ConnectionInterface connection)
	{
	}

	/**
	 * @deprecated Use {@link #getDataSource()} instead.
	 */
	public DataSourceInterface getDataSourceInterface()
	{
		return getDataSource();
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setDataSourceInterface(DataSourceInterface dataSource)
	{
	}

	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
}
