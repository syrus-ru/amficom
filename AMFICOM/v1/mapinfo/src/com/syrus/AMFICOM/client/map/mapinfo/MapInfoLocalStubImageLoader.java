/*
 * $Id: MapInfoLocalStubImageLoader.java,v 1.1.2.5 2005/07/04 14:38:01 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.5 $, $Date: 2005/07/04 14:38:01 $
 * @module mapinfo_v1
 */
public class MapInfoLocalStubImageLoader implements MapImageLoader
{

	private MapJLocalRenderer renderer;

	private MapInfoConnection connection;

	public MapInfoLocalStubImageLoader(MapInfoConnection connection)
			throws MapConnectionException
	{
		this.connection = connection;

		//Setting logger to log nothing.
		System.setProperty("org.apache.commons.logging.Log",
				"com.syrus.AMFICOM.client.map.EmptyLog");

		try
		{
			this.renderer = new MapJLocalRenderer(this.connection.getPath()
					+ this.connection.getView());
		} catch (IOException e)
		{
			throw new MapConnectionException("Failed initializing MapJLocalRenderer");
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#stopRenderingAtServer()
	 */
	public void stopRendering()
	{
		try
		{
			this.renderer.cancelRendering();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapImageLoader#renderMapImageAtServer(com.syrus.AMFICOM.Client.Map.TopologicalRequest)
	 */
	public Image renderMapImage(TopologicalImageQuery query)
			throws MapConnectionException, MapDataException
	{
		//Здесь должен формироваться запрос serverQuery к пулу
		Image image = null;
		try
		{
			image = this.renderer.renderImage(query);
		} catch (Exception e)
		{
			throw new MapDataException("Failed rendering image");
		}

		return image;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException
	{
		return this.connection;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#findSpatialObjects(java.lang.String)
	 */
	public List findSpatialObjects(String searchText)
			throws MapConnectionException, MapDataException
	{
		List<MapInfoSpatialObject> searchResultsList = 
			new ArrayList<MapInfoSpatialObject>();
		
		Iterator layersIt = this.connection.getLocalMapJ().getLayers().iterator(
				LayerType.FEATURE);

		for (; layersIt.hasNext();)
		{
			FeatureLayer currLayer = (FeatureLayer) layersIt.next();
			try
			{
				// Название колонки с надписями
				List labelColumnsList = currLayer.getLabelProperties()
						.getLabelColumns();

				if (labelColumnsList.isEmpty())
					continue;

				FeatureSet fs = currLayer.searchAll(labelColumnsList, null);

				Feature feature = null;
				// Loop until FeatureSet.getNextFeature() returns null
				while ((feature = fs.getNextFeature()) != null)
				{
					String featureName = feature.getAttribute(0).getString();

					if (featureName.toLowerCase().indexOf(searchText.toLowerCase()) < 0)
						continue;

					Log.debugMessage("MapInfoLocalStubImageLoader.findSpatialObjects | "
							+ "Got feature name: " + featureName, Log.FINEST);

					com.mapinfo.util.DoublePoint featureCentre = feature.getGeometry()
							.getBounds().center();

					MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(
							new DoublePoint(featureCentre.x, featureCentre.y), featureName);
					
					searchResultsList.add(spatialObject);
				}
			} catch (Exception exc)
			{
				Log.debugMessage("MapInfoLocalStubImageLoader.findSpatialObjects | "
						+ "ERROR!!! - Failed searching at layer \"" + currLayer.getName()
						+ "\" with message \"" + exc.getMessage() + "\".", Log.FINEST);
			}
		}
		
		return searchResultsList;
	}
}
