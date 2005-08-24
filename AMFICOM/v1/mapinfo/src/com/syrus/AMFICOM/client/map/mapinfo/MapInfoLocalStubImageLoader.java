/*
 * $Id: MapInfoLocalStubImageLoader.java,v 1.14 2005/08/24 08:02:41 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Image;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.mapinfo.dp.Attribute;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.QueryParams;
import com.mapinfo.mapj.FeatureLayer;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapConnectionListener;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/08/24 08:02:41 $
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

	public Set<SpatialObject> findSpatialObjects(SpatialLayer layer, String searchText) throws MapConnectionException, MapDataException {
		final Set<SpatialObject> searchResultsSet = new TreeSet<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();
		//Список колонок с надписями - ищем во всех
		final List labelColumnNames = currLayer.getLabelProperties().getLabelColumns();
		//Список операторов к колонкам
	
		Attribute attributeToFind = new Attribute(searchText);
		for (String columnName : (List<String>)labelColumnNames){
			try {
				final FeatureSet featureSet = currLayer.searchByAttribute(
						labelColumnNames,
						columnName,
						attributeToFind,
						QueryParams.ALL_PARAMS);
				
				Feature feature = null;
				while ((feature = featureSet.getNextFeature()) != null) {
					final String featureName = feature.getAttribute(0).getString();
					final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
						
					final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
							featureName);
	
					searchResultsSet.add(spatialObject);
				}
			} catch (Exception e) {
				throw new MapDataException("Error while searching at region");
			}
		}

		return searchResultsSet;
	}

	public Set<SpatialObject> findSpatialObjects(SpatialLayer layer, Double bounds) throws MapConnectionException, MapDataException {
		final com.mapinfo.util.DoubleRect areaBounds = new com.mapinfo.util.DoubleRect(
				bounds.getMinX(),
				bounds.getMinY(),
				bounds.getMaxX(),
				bounds.getMaxY());
		
		final Set<SpatialObject> searchResultsSet = new TreeSet<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();
		try {
			final FeatureSet featureSet = currLayer.searchWithinRectangle(
					currLayer.getLabelProperties().getLabelColumns(),
					areaBounds,
					QueryParams.ALL_PARAMS);
			
			Feature feature = null;
			while ((feature = featureSet.getNextFeature()) != null) {
				final String featureName = feature.getAttribute(0).getString();
				final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
					
				final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
						featureName);

				searchResultsSet.add(spatialObject);
			}
		} catch (Exception e) {
			throw new MapDataException("Error while searching at region");
		}
		
		return searchResultsSet;
	}
}
