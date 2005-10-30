/*
 * $Id: MapJLocalRenderer.java,v 1.9 2005/10/30 14:49:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;

import com.mapinfo.graphics.LocalRenderer;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/30 14:49:00 $
 * @module mapinfo
 */
public class MapJLocalRenderer {
	public static final int NUM_OF_COLORS = 256;

	public static final Color BACKGROUND_COLOR = Color.WHITE;

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

	private boolean stopRendering = false;

	public MapJLocalRenderer(final String fileToLoad) throws IOException {
		Log.debugMessage("RunningThread - Constructor - Initializing MapJ.", Level.FINEST);
		this.initializeMapJ();
		this.setMapDefinition(fileToLoad);		
	}

	/**
	 * Создаёт объект MapJ и загружает картографические данные.
	 */
	public void initializeMapJ(){
		Log.debugMessage("RunningThread - Initializing MapJ instance...", Level.FINEST);
		//instantiate a MapJ and set the bounds
		this.mapJObject = new MapJ(); // this MapJ object
		this.mapJObject.setDistanceUnits(LinearUnit.meter);
	}

	public void setMapDefinition(final String fileToLoad) throws IOException {
		if (this.mapJObject == null)
			return;
		
		try {
			Log.debugMessage("RunningThread - Loading geoset...", Level.FINEST);
			this.mapJObject.loadMapDefinition(fileToLoad);
			Log.debugMessage("RunningThread - Map definition " + fileToLoad + " has been loaded.", Level.FINEST);
		} catch (IOException e) {
			Log.debugMessage("RunningThread - ERROR!!! - Can't load geoset: " + fileToLoad, Level.SEVERE);
			throw e;
		}
	}
	
	public Image renderImage(final TopologicalImageQuery query) throws Exception {
		this.stopRendering = false;

		final int miWidth = query.getMapImageWidth();
		final int miHeight = query.getMapImageHeight();
		// Setting size, zoom and center point
		if ((this.imageBuffer == null) || (this.imageBuffer.getWidth() != miWidth) || (this.imageBuffer.getHeight() != miHeight)) {
			this.imageBuffer = new BufferedImage(miWidth, miHeight, BufferedImage.TYPE_USHORT_565_RGB);
			Log.debugMessage("RunningThread - Constructor - Creating MapXtreme renderer.", Level.FINEST);
			
			this.renderer = new LocalRenderer(this.imageBuffer);
			Log.debugMessage("RunningThread - Constructor - MapXtreme renderer created.", Level.FINEST);
		}

		this.setSize(miWidth, miHeight);
		this.setCenter(new DoublePoint(query.getTopoCenterX(), query.getTopoCenterY()));
		this.setScale(query.getTopoScale());

		final boolean[] layerVisibilities = query.getLayerVisibilities();
		final boolean[] labelVisibilities = query.getLabelVisibilities();

		for (int i = 0; i < query.getLayerVisibilities().length; i++) {
			this.setLayerVisibility(i, layerVisibilities[i], labelVisibilities[i]);
		}

		final long time1 = System.currentTimeMillis();
		this.renderer.render(ImageRequestComposer.create(this.mapJObject, NUM_OF_COLORS, BACKGROUND_COLOR, "image/gif"));

		if (this.stopRendering) {
			return null;
		}

		final long time2 = System.currentTimeMillis();

		final BufferedImage imageToReturn = new BufferedImage(miWidth, miHeight, BufferedImage.TYPE_USHORT_565_RGB);
		this.imageBuffer.copyData(imageToReturn.getRaster());

		final long time3 = System.currentTimeMillis();

		Log.debugMessage("total " + (time3 - time1) + "\n		"
				+ (time2 - time1) + " (rendered image)\n"
				+ "\n		" + (time3 - time2) + " created new BufferedImage.", Level.FINE);

		return imageToReturn;
	}

	public void cancelRendering() throws Exception {
		this.stopRendering = true;
		Log.debugMessage("MapJRenderer - cancelRendering - Stopping the rendering of map.", Level.FINEST);
		if (this.renderer != null) {
			this.renderer.interrupt();
		}
		Log.debugMessage("MapJRenderer - cancelRendering - Rendering stopped.", Level.FINEST);
	}

	// ---------------------------------------Функции, работающие с MapJ
	// --------------------------
	/**
	 * Установить размер выходного изображения
	 * @param width Ширина
	 * @param height Высота
	 */
	public void setSize(final int width, final int height) {
		synchronized (this.mapJObject) {
			Log.debugMessage("RunningThread - Setting size", Level.FINEST);
			this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
		}
	}

	/**
	 * Установить центральную точку вида карты
	 * 
	 * @param center
	 *        Топологические координаты центральной точки
	 */
	public void setCenter(final DoublePoint center) {
		synchronized (this.mapJObject) {
			Log.debugMessage("RunningThread - Setting center", Level.FINEST);
			try {
				this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			} catch (Exception e) {
				Log.debugMessage("RunningThread - ERROR!!! - Failed setting center.", Level.SEVERE);
			}
		}
	}

	/**
	 * Установить заданный масштаб вида карты
	 * 
	 * @param scale
	 *        Массштаб для карты
	 */
	public void setScale(final double scale) {
		synchronized (this.mapJObject) {
			Log.debugMessage("RunningThread - Setting scale", Level.FINEST);
			try {
				if (scale != 0.0D) {
					this.mapJObject.setZoom(scale);
				}
			} catch (Exception e) {
				Log.debugMessage("RunningThread - ERROR!!! - Failed setting scale.", Level.SEVERE);
			}
		}
	}

	/**
	 * @param layerIndex
	 *        Индекс слоя, которому устанавливаются параметры видимости
	 * @param layerVisible
	 *        Видимость слоя
	 * @param layerLabelsVisible
	 *        Видимость надписей данного слоя
	 */
	public void setLayerVisibility(final int layerIndex, final boolean layerVisible, final boolean layerLabelsVisible) {
		synchronized (this.mapJObject) {
			try {
				final FeatureLayer layer = (FeatureLayer) this.mapJObject.getLayers().get(layerIndex, LayerType.FEATURE);

				Log.debugMessage("RunningThread - Setting visibility for layer " + layer.getName(), Level.FINEST);

				layer.setEnabled(layerVisible);
				layer.setAutoLabel(layerLabelsVisible);
			} catch (Exception exc) {
				Log.debugMessage("RunningThread - ERROR!!! - Failed setting layer visibility.", Level.SEVERE);
			}
		}
	}

	public MapJ getMapJObject() {
		return this.mapJObject;
	}
}
