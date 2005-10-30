/*
 * $Id: MapInfoLocalStubImageLoader.java,v 1.22 2005/10/30 14:49:00 bass Exp $
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

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
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/10/30 14:49:00 $
 * @module mapinfo
 */
public class MapInfoLocalStubImageLoader implements MapImageLoader, MapConnectionListener {
	private final static String FIRST_SEARCH_STRING = "XXX";
	private MapJLocalRenderer renderer;

	private MapInfoConnection connection;
	private boolean alreadyPerformed = false;

	public MapInfoLocalStubImageLoader(final MapInfoConnection connection) throws MapConnectionException {
		this.connection = connection;

		// Setting logger to log nothing.
		System.setProperty("org.apache.commons.logging.Log", "com.syrus.AMFICOM.client.map.EmptyLog");
		try {
			this.renderer = new MapJLocalRenderer(this.connection.getPath());

//			checkFirstSearch();
//		} catch (MapDataException e) {
//			throw new MapConnectionException("Error while first search.");
		} catch (IOException e) {
			throw new MapConnectionException("Failed initializing MapJLocalRenderer");
		}
	}

	/**
	 * @throws MapDataException
	 * @throws MapConnectionException
	 */
	private void checkFirstSearch() throws MapDataException, MapConnectionException {
		if(!this.alreadyPerformed ) {
			//Осуществляется ПЕРВЫЙ поиск по всем слоям с надписями - тот, который сильно тормозит из-за
			//MapJшного кэширования таблиц.
			Log.debugMessage("Starting first search.", Level.FINE);
			long t1 = System.currentTimeMillis();
			this.alreadyPerformed = true;
			for (SpatialLayer spatialLayer : this.connection.getLayers()){
				if (!this.connection.searchIsAvailableForLayer(spatialLayer))
					continue;
	
				MapInfoSpatialLayer miSpatialLayer = (MapInfoSpatialLayer)spatialLayer;
				if (miSpatialLayer.getFeatureLayer().getLabelProperties().getLabelColumns().size() != 0)
					this.findSpatialObjects(miSpatialLayer,FIRST_SEARCH_STRING);
			}
			long t2 = System.currentTimeMillis();			
			Log.debugMessage("First search completed ( "+ (t2 - t1) + " ms).", Level.FINE);
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
		checkFirstSearch();
		String minimizedSearchText = searchText.toLowerCase();
		
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();

		//Список колонок с надписями - ищем во всех
		final List columnNames = currLayer.getLabelProperties().getLabelColumns();
		
//		TableInfo tableInfo = currLayer.getTableInfo();
//		int columnsCount = tableInfo.getColumnCount();
//		final List<String> columnNames = new ArrayList<String>(columnsCount);
//		for (int i = 0; i < columnsCount; i++)
//			columnNames.add(tableInfo.getColumnName(i));
	
		try {
			long sumGettingFeature = 0;
			long sumGettingStringAttributes = 0;
			long sumComparingStrings = 0;
			long sumGettingCenters = 0;
			long sumCreatingObjects = 0;			
			
			long t1 = System.currentTimeMillis();
			final FeatureSet featureSet = currLayer.searchAll(
					columnNames,
					QueryParams.GEOM_ONLY_PARAMS);
				
			long t2 = System.currentTimeMillis();
			
			Feature feature = null;
			String featureName = null;
		
			long t2start = System.currentTimeMillis();
			long t2end = System.currentTimeMillis();			
			while ((feature = featureSet.getNextFeature()) != null) {
				t2start = System.currentTimeMillis();
				sumGettingFeature += (t2start - t2end);

				boolean toAdd = false;
				for (int i = 0; i < columnNames.size(); i++){
					long t23 = System.currentTimeMillis();					
					featureName = feature.getAttribute(i).getString();
					long t24 = System.currentTimeMillis();
					sumGettingStringAttributes += (t24 - t23);					
					if (featureName.toLowerCase().contains(minimizedSearchText)){
						toAdd = true;
						break;
					}
					long t25 = System.currentTimeMillis();
					sumComparingStrings += (t25 - t24);
				}

				if (!toAdd){
					t2end = System.currentTimeMillis();					
					continue;
				}
				long t26 = System.currentTimeMillis();				
				final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
				long t27 = System.currentTimeMillis();
				sumGettingCenters += (t27 - t26);
				final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(
						new DoublePoint(featureCentre.x, featureCentre.y),
						featureName);
				long t28 = System.currentTimeMillis();				
				sumCreatingObjects += (t28 - t27);
				searchResultsList.add(spatialObject);
				t2end = System.currentTimeMillis();				
			}
			long t3 = System.currentTimeMillis();			
			featureSet.dispose();
			
			Log.debugMessage((t2 - t1) + "ms - searching all\n"
					+ (t3 - t2) + "ms - comparing all, particulary:\n"
					+ sumGettingFeature + "ms - sumGettingFeature\n"
					+ sumGettingStringAttributes + "ms - sumGettingStringAttributes\n"
					+ sumComparingStrings + "ms - sumComparingStrings\n"
					+ sumGettingCenters + "ms - sumGettingCenters\n"
					+ sumCreatingObjects + "ms - sumCreatingObjects\n",Level.FINE);
			
		} catch (Exception e) {
			throw new MapDataException("Error while searching at region", e);
		}
	
//		Attribute attributeToFind = new Attribute(searchText);
//		for (String columnName : (List<String>)labelColumnNames){
//			try {
//				final FeatureSet featureSet = currLayer.searchByAttribute(
//						labelColumnNames,
//						columnName,
//						attributeToFind,
//						QueryParams.GEOM_ONLY_PARAMS);
//				
//				Feature feature = null;
//				while ((feature = featureSet.getNextFeature()) != null) {
//					final String featureName = feature.getAttribute(0).getString();
//					final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
//						
//					final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
//							featureName);
//	
//					searchResultsSet.add(spatialObject);
//				}
//				featureSet.dispose();
//			} catch (Exception e) {
//				throw new MapDataException("Error while searching at region", e);
//			}
//		}
		Collections.sort(searchResultsList);
		return searchResultsList;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, Double bounds) throws MapConnectionException, MapDataException {
		final com.mapinfo.util.DoubleRect areaBounds = new com.mapinfo.util.DoubleRect(
				bounds.getMinX(),
				bounds.getMinY(),
				bounds.getMaxX(),
				bounds.getMaxY());
		
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();

		final FeatureLayer currLayer = ((MapInfoSpatialLayer)layer).getFeatureLayer();
		try {
			final FeatureSet featureSet = currLayer.searchWithinRectangle(
					currLayer.getLabelProperties().getLabelColumns(),
					areaBounds,
					QueryParams.GEOM_ONLY_PARAMS);
			
			Feature feature = null;
			while ((feature = featureSet.getNextFeature()) != null) {
				final String featureName = feature.getAttribute(0).getString();
				final com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry().getBounds().center();
					
				final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(featureCentre.x, featureCentre.y),
						featureName);

				searchResultsList.add(spatialObject);
			}
			Collections.sort(searchResultsList);
		} catch (Exception e) {
			throw new MapDataException("Error while searching at region");
		}
		
		return searchResultsList;
	}
}
