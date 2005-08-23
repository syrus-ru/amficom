/*
 * $Id: MapInfoLocalStubImageLoader.java,v 1.10 2005/08/23 10:12:14 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Image;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapConnectionListener;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.10 $, $Date: 2005/08/23 10:12:14 $
 * @module mapinfo
 */
public class MapInfoLocalStubImageLoader implements MapImageLoader, MapConnectionListener {

	private MapJLocalRenderer renderer;

	private MapInfoConnection connection;

	public MapInfoLocalStubImageLoader(final MapInfoConnection connection) throws MapConnectionException {
		this.connection = connection;

		// Setting logger to log nothing.
		System.setProperty("org.apache.commons.logging.Log", "com.syrus.AMFICOM.client.map.EmptyLog");

		try {
			this.renderer = new MapJLocalRenderer(this.connection.getPath());
		} catch (IOException e) {
			throw new MapConnectionException("Failed initializing MapJLocalRenderer");
		}
	}

	public void mapConnectionChanged() throws MapConnectionException {
		try {
			this.renderer.setMapDefinition(this.connection.getPath());
		} catch (IOException e) {
			throw new MapConnectionException("Failed initializing MapJLocalRenderer");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#stopRenderingAtServer()
	 */
	public void stopRendering() {
		try {
			this.renderer.cancelRendering();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.Client.Map.TopologicalRequest)
	 */
	public Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException {
		// Здесь должен формироваться запрос serverQuery к пулу
		try {
			return this.renderer.renderImage(query);
		} catch (Exception e) {
			throw new MapDataException("Failed rendering image");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException {
		return this.connection;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, String searchText) throws MapConnectionException, MapDataException {
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();
		try {
			// Название колонки с надписями
			final List labelColumnsList = currLayer.getLabelProperties().getLabelColumns();

			if (!labelColumnsList.isEmpty()) {
				final FeatureSet fs = currLayer.searchAll(labelColumnsList, null);
	
				Feature feature = null;
				// Loop until FeatureSet.getNextFeature() returns null
				while ((feature = fs.getNextFeature()) != null) {
					final String featureName = feature.getAttribute(0).getString();
	
					if (featureName.toLowerCase().indexOf(searchText.toLowerCase()) < 0) {
						continue;
					}
	
					Log.debugMessage("MapInfoLocalStubImageLoader.findSpatialObjects | " + "Got feature name: " + featureName, Level.FINEST);
	
					final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
	
					final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
							featureName);
	
					searchResultsList.add(spatialObject);
				}
			}
		} catch (Exception exc) {
			Log.errorMessage("MapInfoLocalStubImageLoader.findSpatialObjects | "
					+ "ERROR!!! - Failed searching at layer \"" + currLayer.getName()
					+ "\" with message \"" + exc.getMessage() + "\".");
		}

		return searchResultsList;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, Double bounds) throws MapConnectionException, MapDataException {
		final com.mapinfo.util.DoubleRect areaBounds = new com.mapinfo.util.DoubleRect(
				bounds.x,
				bounds.y,
				bounds.width,
				bounds.height);
		
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();
		try {
			// Название колонки с надписями
			final List labelColumnsList = currLayer.getLabelProperties().getLabelColumns();

			if (!labelColumnsList.isEmpty()) {
				final FeatureSet fs = currLayer.searchAll(labelColumnsList, null);
	
				Feature feature = null;
				// Loop until FeatureSet.getNextFeature() returns null
				while ((feature = fs.getNextFeature()) != null) {
					final String featureName = feature.getAttribute(0).getString();
	
					final com.mapinfo.util.DoubleRect featureBounds = feature.getGeometry().getBounds();
					final com.mapinfo.util.DoublePoint featureCentre = featureBounds.center();
					if (!featureBounds.intersects(areaBounds))
						continue;
					
					final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
							featureName);
	
					searchResultsList.add(spatialObject);
				}
			}
		} catch (Exception exc) {
			Log.errorMessage("MapInfoLocalStubImageLoader.findSpatialObjects | "
					+ "ERROR!!! - Failed searching at layer \"" + currLayer.getName()
					+ "\" with message \"" + exc.getMessage() + "\".");
		}

		return searchResultsList;
	}
}
