package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.General.Model.Environment;

public abstract class MapConnection 
{
	public abstract boolean connect();
	public abstract boolean release();
	
	public abstract void setPath(String path);
	public abstract void setView(String name);

	public abstract String getPath();
	public abstract String getView();

	public static MapConnection create(String connectionClass)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call MapConnection.create()");
		
		MapConnection connection = null;
		try
		{
			connection = (MapConnection )Class.forName(connectionClass).newInstance();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		catch(InstantiationException ie)
		{
			ie.printStackTrace();
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
		}
			
		return connection;
	}
}
