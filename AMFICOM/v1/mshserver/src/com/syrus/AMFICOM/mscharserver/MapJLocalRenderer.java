/*-
 * $Id: MapJLocalRenderer.java,v 1.3 2005/06/17 12:38:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mapinfo.graphics.LocalRenderer;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/17 12:38:52 $
 * @module mscharserver_v1
 */
public class MapJLocalRenderer
{
	
	// Render preferences
	public static final int NUM_OF_COLORS = 256;
	public static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final String	MAPINFO_DEFINITION_FILE_LOCATION_KEY	= "MapinfoDefinitionFile";
	private static final String	DEFAULT_MDF_FILE_LOCATION	= "../lib/mapinfo/cb.mdf";
	/**
	 * MapInfo data to image renderer
	 */
	private LocalRenderer renderer = null;
	
	/**
	 * Image where the map will be renderered
	 */
	private BufferedImage imageBuffer = null;
	
	/**
	 * MapInfo topological information container
	 */
	private MapJ mapJObject = null;
	
	/**
	 * Flag is true when the rendering is stopped and
	 * the image is not to be encoded.
	 */
	private boolean cancelEncoding = false;
	
	public MapJLocalRenderer()
		throws IllegalDataException  
	{
		String fileToLoad = ApplicationProperties.getString(MAPINFO_DEFINITION_FILE_LOCATION_KEY, DEFAULT_MDF_FILE_LOCATION);
		Log.debugMessage("MapJLocalRenderer<init> | RunningThread - Constructor - Initializing MapJ.", Log.DEBUGLEVEL07);		 
		this.mapJObject = createMapJ(fileToLoad);
	}
	
	public static MapJ createMapJ(String fileToLoad) throws IllegalDataException {
		Log.debugMessage("MapJLocalRenderer.createMapJ |" +
				" RunningThread - Initializing MapJ instance...", Log.DEBUGLEVEL07);
		// instantiate a MapJ and set the bounds
		MapJ returnValue = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try {
			Log.debugMessage("MapJLocalRendererRunningThread.createMapJ |" +
					" Loading geoset...", Log.DEBUGLEVEL07);
			returnValue.loadMapDefinition(fileToLoad);
			Log.debugMessage("MapJLocalRendererRunningThread.createMapJ |" +
					" - Map definition " + fileToLoad
					+ " has been loaded.", Log.DEBUGLEVEL07);
		} catch (IOException e) {
			String msg = "MapJLocalRendererRunningThread.createMapJ |" +
					" - ERROR!!! - Can't load geoset: " + fileToLoad;
			Log.errorMessage(msg);
			throw new IllegalDataException(msg);
		}

		returnValue.setDistanceUnits(LinearUnit.meter);

		return returnValue;
	}
	
	public MapJ getMapJObject()
	{
		return this.mapJObject;
	}
	public void setMapJObject(MapJ mapJObject)
	{
		this.mapJObject = mapJObject;
	}
	public LocalRenderer getRenderer()
	{
		return this.renderer;
	}
	public void setRenderer(LocalRenderer renderer)
	{
		this.renderer = renderer;
	}
	
	public byte[] render(TopologicalImageQuery query) throws Exception {
		this.cancelEncoding = false;

		int miWidth = query.getMapImageWidth();
		int miHeight = query.getMapImageHeight();
		// Setting size, zoom and center point
		if ((this.imageBuffer == null)
				|| (this.imageBuffer.getWidth() != miWidth)
				|| (this.imageBuffer.getHeight() != miHeight)) {
			this.imageBuffer = new BufferedImage(miWidth, miHeight,
					BufferedImage.TYPE_USHORT_565_RGB);
			Log.debugMessage("MapJLocalRenderer.render | RunningThread - Constructor - Creating MapXtreme renderer.",
							Log.DEBUGLEVEL07);
			this.renderer = new LocalRenderer(this.imageBuffer);
			Log.debugMessage("MapJLocalRenderer.render | RunningThread - Constructor - MapXtreme renderer created.",
							Log.DEBUGLEVEL07);
		}

		this.setSize(miWidth, miHeight);
		this.setCenter(new DoublePoint(query.getTopoCenterX(), query
				.getTopoCenterY()));
		this.setScale(query.getTopoScale());

		boolean[] layerVisibilities = query.getLayerVisibilities();
		boolean[] labelVisibilities = query.getLabelVisibilities();

		for (int i = 0; i < query.getLayerVisibilities().length; i++)
			this.setLayerVisibility(i, layerVisibilities[i],
					labelVisibilities[i]);

		if (this.renderer == null) {
			Log.errorMessage("MapJLocalRenderer.render | RunningThread - Constructor - Renderer is not initialized. "
							+ "Run setRenderingParameters first.");
			throw new IllegalDataException("MapJLocalRenderer.render | Failed to initialize Renderer");
		}

		Log.debugMessage("MapJLocalRenderer.render | RenderToStream - Before rendering.",
						Log.DEBUGLEVEL07);

		try {
			this.renderer.render(ImageRequestComposer.create(this.mapJObject,
					NUM_OF_COLORS, BACKGROUND_COLOR, "image/png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		if (this.cancelEncoding)
			return null;

		// Output the map as a GIF Image
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		Log.debugMessage("MapJLocalRenderer.render | RenderToStream - Encoding image.",
				Log.DEBUGLEVEL07);
		ImageIO.write(this.imageBuffer, "PNG", resultStream);

		resultStream.flush();
		resultStream.close();

		byte[] result = resultStream.toByteArray();
		Log.debugMessage("MapJLocalRenderer.render | RenderToStream - Successfully rendered.",
						Log.DEBUGLEVEL07);
		return result;
	}
	
	public void cancelRendering()
		throws Exception
	{
		Log.debugMessage("MapJLocalRenderer.cancelRendering | Stopping the rendering of map.", Log.DEBUGLEVEL07);  
		
		this.cancelEncoding = true;
		this.renderer.interrupt();
		
		Log.debugMessage("MapJLocalRenderer.cancelRendering | Rendering stopped.", Log.DEBUGLEVEL07);	  
	}
	
//---------------------------------------Функции, работающие с MapJ --------------------------
	/**
	 * Установить размер выходного изображения
	 * @param width Ширина
	 * @param height Высота
	 */
	public void setSize(int width, int height)
	{
		synchronized (this.mapJObject)
		{
			Log.debugMessage("MapJLocalRenderer.setSize | RunningThread - Setting size", Log.DEBUGLEVEL07);	 
			this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
		}
	}
	
	/**
	 * Установить центральную точку вида карты
	 * @param center Топологические координаты центральной точки
	 */
	public void setCenter(DoublePoint center)
	{
		synchronized (this.mapJObject)
		{
			Log.debugMessage("MapJLocalRenderer.setCenter | RunningThread - Setting center", Log.DEBUGLEVEL07);	   
			try
			{
				this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			} catch (Exception e)
			{
				Log.errorMessage("MapJLocalRenderer.setCenter | RunningThread - ERROR!!! - Failed setting center. Reason" + e.getMessage());
			}
		}
	}

	/**
	 * Установить заданный масштаб вида карты
	 * @param scale Массштаб для карты
	 */
	public void setScale(double scale)
	{
		synchronized (this.mapJObject)
		{
			Log.debugMessage("MapJLocalRenderer.setScale | RunningThread - Setting scale", Log.DEBUGLEVEL07);		
			try
			{
				if (scale != 0.0D)
					this.mapJObject.setZoom(scale);
			} catch (Exception e)
			{
				Log.errorMessage("MapJLocalRenderer.setScale| RunningThread - ERROR!!! - Failed setting scale.");
			}
		}
	}

	/**
	 * @param layerIndex Индекс слоя, которому устанавливаются параметры видимости
	 * @param layerVisible Видимость слоя
	 * @param layerLabelsVisible Видимость надписей данного слоя
	 */
	public void setLayerVisibility(
		int layerIndex,
		boolean layerVisible,
		boolean layerLabelsVisible)
	{
		synchronized (this.mapJObject)
		{
			try
			{
				FeatureLayer layer = (FeatureLayer)this.mapJObject.getLayers().get(
					layerIndex, LayerType.FEATURE);
				
				Log.debugMessage("MapJLocalRenderer.setLayerVisibility | RunningThread - Setting visibility for layer " + layer.getName(), Log.DEBUGLEVEL07);
				
				layer.setEnabled(layerVisible);
				layer.setAutoLabel(layerLabelsVisible);
			} catch (Exception exc)
			{
				Log.errorMessage("MapJLocalRenderer.setLayerVisibility | RunningThread - ERROR!!! - Failed setting layer visibility. Reason: " + exc.getMessage());
			}
		}
	}
}
