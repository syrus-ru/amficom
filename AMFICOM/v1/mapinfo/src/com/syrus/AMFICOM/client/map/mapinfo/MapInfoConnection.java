package com.syrus.AMFICOM.client.map.mapinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.util.Log;

public abstract class MapInfoConnection extends MapConnection
{
	protected String dataBasePath = "";

	protected String dataBaseView = "";

	protected String mapperServletURL = "";
	
	protected MapJ localMapJ = null;

	/**
	 * Список слоёв. Подгружается один раз при инциализации модуля.
	 * Следует обновлять при изменении файла проекта во время работы (это опция пока нереализована)
	 */
	private List layersList = null;
	
	public boolean connect() throws MapConnectionException
	{
		Log.debugMessage(getClass()
				.getName() + "::" + "connect()" + " | " + "method call", Log.FINER);
		
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
		Log.debugMessage(getClass()
				.getName() + "::" + "release()" + " | " + "method call", Log.FINER);

		return true;
	}

	public void setPath(String path)
	{
		Log.debugMessage(getClass()
				.getName() + "::" + "setPath(" + path + ")" + " | " + "method call", Log.FINER);

		this.dataBasePath = path;
	}

	public void setView(String name)
	{
		Log.debugMessage(getClass()
				.getName() + "::" + "setView(" + name + ")" + " | " + "method call", Log.FINER);

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
		Log.debugMessage(getClass()
				.getName() + "::" + "setURL(" + mapperURL + ")" + " | " + "method call", Log.FINER);
		
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
	
	public MapCoordinatesConverter createCoordinatesConverter()
	{
		return new MapInfoCoordinatesConverter(this);
	}
	
	public MapContext createMapContext()
	{
		return new MapInfoContext(this);
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapContext#getLayers()
	 */
	public List getLayers() throws MapDataException
	{
		if (this.layersList == null)
		{
			this.layersList = new ArrayList();

			Iterator layersIt = this.localMapJ.getLayers().iterator(
					LayerType.FEATURE);
			for(; layersIt.hasNext();)
			{
				FeatureLayer currLayer = (FeatureLayer) layersIt.next();
				//TODO Здесь должен быть конструктор только от Featurelayer'а
				SpatialLayer spL = new MapInfoSpatialLayer(currLayer);
				this.layersList.add(spL);
			}
		}

		return this.layersList;
	}

}
