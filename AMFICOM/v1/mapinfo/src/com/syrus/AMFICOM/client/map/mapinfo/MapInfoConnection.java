/*
 * $Id: MapInfoConnection.java,v 1.10 2005/08/11 13:55:50 arseniy Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * оБХЮОП-ФЕИОЙЮЕУЛЙК ГЕОФТ.
 * рТПЕЛФ: бнжйлпн.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/08/11 13:55:50 $
 * @author $Author: arseniy $
 * @module mapinfo
 */
public abstract class MapInfoConnection extends MapConnection {
	protected String dataBasePath = "";

	protected String dataBaseView = "";

	protected String mapperServletURL = "";

	protected MapJ localMapJ = null;

	/**
	 * Список слоёв. Подгружается один раз при инциализации модуля.
	 * Следует обновлять при изменении файла проекта во время работы (это опция пока нереализована)
	 */
	private List<SpatialLayer> layersList = null;

	@Override
	public boolean connect() throws MapConnectionException {
		Log.debugMessage(getClass().getName() + "::" + "connect()" + " | " + "method call", Level.FINER);

		// Инициализируем объект MapJ для локальных преобразований координат
		this.localMapJ = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		final String mapDefinitionFile = this.getPath() + this.getView();
		try {
			System.out.println("MapImagePanel - Loading geoset...");
			this.localMapJ.loadMapDefinition(mapDefinitionFile);
			System.out.println("MapImagePanel - Geoset " + mapDefinitionFile + " has been loaded.");
		} catch (IOException e) {
			System.out.println("MapImagePanel - Can't load geoset: " + mapDefinitionFile);
			throw new MapConnectionException(e);
		}

		System.out.println("Units " + this.localMapJ.getDistanceUnits().toString());
		this.localMapJ.setDistanceUnits(LinearUnit.meter);

		return true;
	}

	@Override
	public boolean release() throws MapConnectionException {
		Log.debugMessage(getClass().getName() + "::" + "release()" + " | " + "method call", Level.FINER);

		return true;
	}

	@Override
	public void setPath(final String path) {
		Log.debugMessage(getClass().getName() + "::" + "setPath(" + path + ")" + " | " + "method call", Level.FINER);

		this.dataBasePath = path;
	}

	@Override
	public void setView(final String name) {
		Log.debugMessage(getClass().getName() + "::" + "setView(" + name + ")" + " | " + "method call", Level.FINER);

		this.dataBaseView = name;
	}

	@Override
	public String getPath() {
		return this.dataBasePath;
	}

	@Override
	public String getView() {
		return this.dataBaseView;
	}

	@Override
	public void setURL(final String mapperURL) {
		Log.debugMessage(getClass().getName() + "::" + "setURL(" + mapperURL + ")" + " | " + "method call", Level.FINER);

		this.mapperServletURL = mapperURL;
	}

	@Override
	public String getURL() {
		return this.mapperServletURL;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	@Override
	public List<String> getAvailableViews() throws MapDataException {
		final List<String> listToReturn = new ArrayList<String>();
		listToReturn.add(this.getPath() + this.getView());

		return listToReturn;
	}

	public MapJ getLocalMapJ() {
		return this.localMapJ;
	}

	@Override
	public MapCoordinatesConverter createCoordinatesConverter() {
		return new MapInfoCoordinatesConverter(this);
	}

	@Override
	public MapContext createMapContext() {
		return new MapInfoContext(this);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapContext#getLayers()
	 */
	@Override
	public List<SpatialLayer> getLayers() throws MapDataException {
		if (this.layersList == null) {
			this.layersList = new ArrayList<SpatialLayer>();

			for (final Iterator layersIt = this.localMapJ.getLayers().iterator(LayerType.FEATURE); layersIt.hasNext();) {
				final FeatureLayer currLayer = (FeatureLayer) layersIt.next();
				//TODO Здесь должен быть конструктор только от Featurelayer'а
				final SpatialLayer spL = new MapInfoSpatialLayer(currLayer);
				this.layersList.add(spL);
			}
		}

		return this.layersList;
	}

}
