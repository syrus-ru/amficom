package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.syrus.AMFICOM.Client.Map.MapConnection;

public class MapInfoConnection extends MapConnection
{
	public boolean connect()
	{
		return false;
	}

	public boolean release()
	{
		return false;
	}

	public void setPath(String path)
	{
	}
	
	public void setView(String name)
	{
	}

	public String getPath()
	{
		return null;
	}
	
	public String getView()
	{
		return null;
	}
}
