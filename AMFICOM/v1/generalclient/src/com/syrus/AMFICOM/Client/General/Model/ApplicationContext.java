package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

public class ApplicationContext 
{
	ConnectionInterface ci;
	SessionInterface si;
	ApplicationModel aModel;
	DataSourceInterface dataSource;
	Dispatcher dispatcher;

	public ApplicationContext()
	{
	}

	public void setConnectionInterface(ConnectionInterface ci)
	{
		this.ci = ci;
	}

	public void setSessionInterface(SessionInterface si)
	{
		this.si = si;
	}

	public void setApplicationModel(ApplicationModel aModel)
	{
		this.aModel = aModel;
	}

	public void setDataSourceInterface(DataSourceInterface dataSource)
	{
		this.dataSource = dataSource;
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public ConnectionInterface getConnectionInterface()
	{
		return ci;
	}

	public SessionInterface getSessionInterface()
	{
		return si;
	}

	public ApplicationModel getApplicationModel()
	{
		return aModel;
	}

	public DataSourceInterface getDataSourceInterface()
	{
		return dataSource;
	}

	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
}