/*
 * $Id: MapInfoConnection.java,v 1.17 2006/06/22 11:47:14 stas Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
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
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/06/22 11:47:14 $
 * @author $Author: stas $
 * @module mapinfo
 */
public abstract class MapInfoConnection extends MapConnection {
	protected String dataBasePath = "";

	protected String dataBaseView = "";

	protected String mapperServletURL = "";

	protected MapJ localMapJ = null;

	/**
	 * ?????? ?????. ???????????? ???? ??? ??? ???????????? ??????.
	 * ??????? ????????? ??? ????????? ????? ??????? ?? ????? ?????? (??? ????? ???? ?????????????)
	 */
	private List<SpatialLayer> layersList = null;

	@Override
	public boolean connect() throws MapConnectionException {
		Log.debugMessage("method call", Level.FINEST);

		// ?????????????? ?????? MapJ ??? ????????? ?????????????? ?????????
		this.localMapJ = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		final String mapDefinitionFile = this.getPath();
		try {
			Log.debugMessage("MapImagePanel - Loading geoset...", Level.INFO);
			this.localMapJ.loadMapDefinition(mapDefinitionFile);
			Log.debugMessage("MapImagePanel - Geoset " + mapDefinitionFile + " has been loaded.", Level.INFO);
		} catch (IOException e) {
			Log.debugMessage("MapImagePanel - Can't load geoset: " + mapDefinitionFile, Level.SEVERE);
			throw new MapConnectionException(e);
		}

		Log.debugMessage("Units " + this.localMapJ.getDistanceUnits().toString(), Level.FINEST);
		this.localMapJ.setDistanceUnits(LinearUnit.meter);

		fireMapConnectionChanged();

		return true;
	}

	@Override
	public boolean release() {
		Log.debugMessage("method call", Level.FINEST);

		return true;
	}

	@Override
	public void setPath(final String path) {
		Log.debugMessage(path + " | " + "method call", Level.FINEST);

		this.dataBasePath = path;
	}

	@Override
	public void setView(final String name){
		Log.debugMessage(name + " | " + "method call", Level.FINEST);

		this.dataBaseView = name;
	}

	@Override
	public String getPath() {
		return this.dataBasePath;
	}

	@Override
	public void setURL(final String mapperURL) {
		Log.debugMessage(mapperURL + " | " + "method call", Level.FINEST);

		this.mapperServletURL = mapperURL;
	}

	@Override
	public String getURL() {
		return this.mapperServletURL;
	}

	@Override
	public String getView() {
		return this.dataBaseView;
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
	public List<SpatialLayer> getLayers() {
		if (this.layersList == null) {
			this.layersList = new ArrayList<SpatialLayer>();

			for (final Iterator layersIt = this.localMapJ.getLayers().iterator(LayerType.FEATURE); layersIt.hasNext();) {
				final FeatureLayer currLayer = (FeatureLayer) layersIt.next();
				//TODO ????? ?????? ???? ??????????? ?????? ?? Featurelayer'?
				final SpatialLayer spL = new MapInfoSpatialLayer(currLayer);
				this.layersList.add(spL);
			}
		}

		return this.layersList;
	}
}
