package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

public class MapInfoConnection extends MapConnection
{
	protected String dataBasePath = "";

	protected String dataBaseView = "";

	protected String mapperServletURL = "";
	
	protected MapJ localMapJ = null;	

	public MapInfoConnection()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"constructor call",
				getClass().getName(),
				"MapInfoConnection()");
	}

	public boolean connect() throws MapConnectionException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "connect()");
		
		// Инициализируем объект MapJ для локальных преобразований координат
		this.localMapJ = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		String mapDefinitionFile = getPath() + getView();
		try
		{
			System.out.println("MapImagePanel - Loading geoset...");
			this.localMapJ.loadMapDefinition(mapDefinitionFile);
			System.out.println("MapImagePanel - Geoset " + mapDefinitionFile
					+ " has been loaded.");
		}
		catch(IOException e)
		{
			System.out.println("MapImagePanel - Can't load geoset: "
					+ mapDefinitionFile);
			throw new MapConnectionException(e);
		}

		System.out.println("Units " + this.localMapJ.getDistanceUnits().toString());
		this.localMapJ.setDistanceUnits(LinearUnit.meter);
		
		return true;
	}

	public boolean release() throws MapConnectionException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "release()");

		return true;
	}

	public void setPath(String path)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setPath(" + path + ")");

		this.dataBasePath = path;
	}

	public void setView(String name)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setView(" + name + ")");

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

	public void setURL(String mapperURL)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setURL(" + mapperURL + ")");

		this.mapperServletURL = mapperURL;
	}

	public String getURL()
	{
		return this.mapperServletURL;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	public List getAvailableViews() throws MapDataException
	{
		List listToReturn = new ArrayList();
		listToReturn.add(getPath() + getView());

		return listToReturn;
	}

	
	public MapJ getLocalMapJ()
	{
		return this.localMapJ;
	}
}
