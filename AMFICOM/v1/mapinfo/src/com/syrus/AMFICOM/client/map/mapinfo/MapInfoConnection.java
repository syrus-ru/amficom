package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnection;

public class MapInfoConnection extends MapConnection
{
	protected String dataBasePath = "";
	protected String dataBaseView = "";

	public MapInfoConnection()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapInfoConnection()");
	}
	
	public MapInfoConnection( String dataBasePath, String dataBaseView)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapInfoConnection(" + dataBasePath + ", " + dataBaseView + ")");
		
		this.setPath(dataBasePath);
		this.setView(dataBaseView);
	}

	public boolean connect()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"connect()");
		return true;
	}

	public boolean release()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"release()");
		
		return true;
	}

	public void setPath(String path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setPath(" + path + ")");
		
		this.dataBasePath = path;
	}
	
	public void setView(String name)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setView(" + name + ")");

		this.dataBaseView = name;
	}

	public String getPath()
	{
		return this.dataBasePath;
	}
	
	public String getView()
	{
		return this.dataBaseView;
	}
}
