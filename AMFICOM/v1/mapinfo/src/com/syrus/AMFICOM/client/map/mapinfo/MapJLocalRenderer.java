/*
 * $Id: MapJLocalRenderer.java,v 1.10 2005/10/30 15:20:35 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
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
 * @version $Revision: 1.10 $, $Date: 2005/10/30 15:20:35 $
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
		assert Log.debugMessage("RunningThread - Constructor - Initializing MapJ.", Level.FINEST);
		this.initializeMapJ();
		this.setMapDefinition(fileToLoad);		
	}

	/**
	 * ������ ������ MapJ � ��������� ���������������� ������.
	 */
	public void initializeMapJ(){
		assert Log.debugMessage("RunningThread - Initializing MapJ instance...", Level.FINEST);
		//instantiate a MapJ and set the bounds
		this.mapJObject = new MapJ(); // this MapJ object
		this.mapJObject.setDistanceUnits(LinearUnit.meter);
	}

	public void setMapDefinition(final String fileToLoad) throws IOException {
		if (this.mapJObject == null)
			return;
		
		try {
			assert Log.debugMessage("RunningThread - Loading geoset...", Level.FINEST);
			this.mapJObject.loadMapDefinition(fileToLoad);
			assert Log.debugMessage("RunningThread - Map definition " + fileToLoad + " has been loaded.", Level.FINEST);
		} catch (IOException e) {
			assert Log.debugMessage("RunningThread - ERROR!!! - Can't load geoset: " + fileToLoad, Level.SEVERE);
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
			assert Log.debugMessage("RunningThread - Constructor - Creating MapXtreme renderer.", Level.FINEST);
			
			this.renderer = new LocalRenderer(this.imageBuffer);
			assert Log.debugMessage("RunningThread - Constructor - MapXtreme renderer created.", Level.FINEST);
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

		assert Log.debugMessage("total " + (time3 - time1) + "\n		"
				+ (time2 - time1) + " (rendered image)\n"
				+ "\n		" + (time3 - time2) + " created new BufferedImage.", Level.FINE);

		return imageToReturn;
	}

	public void cancelRendering() throws Exception {
		this.stopRendering = true;
		assert Log.debugMessage("MapJRenderer - cancelRendering - Stopping the rendering of map.", Level.FINEST);
		if (this.renderer != null) {
			this.renderer.interrupt();
		}
		assert Log.debugMessage("MapJRenderer - cancelRendering - Rendering stopped.", Level.FINEST);
	}

	// ---------------------------------------�������, ���������� � MapJ
	// --------------------------
	/**
	 * ���������� ������ ��������� �����������
	 * @param width ������
	 * @param height ������
	 */
	public void setSize(final int width, final int height) {
		synchronized (this.mapJObject) {
			assert Log.debugMessage("RunningThread - Setting size", Level.FINEST);
			this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
		}
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 * 
	 * @param center
	 *        �������������� ���������� ����������� �����
	 */
	public void setCenter(final DoublePoint center) {
		synchronized (this.mapJObject) {
			assert Log.debugMessage("RunningThread - Setting center", Level.FINEST);
			try {
				this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			} catch (Exception e) {
				assert Log.debugMessage("RunningThread - ERROR!!! - Failed setting center.", Level.SEVERE);
			}
		}
	}

	/**
	 * ���������� �������� ������� ���� �����
	 * 
	 * @param scale
	 *        �������� ��� �����
	 */
	public void setScale(final double scale) {
		synchronized (this.mapJObject) {
			assert Log.debugMessage("RunningThread - Setting scale", Level.FINEST);
			try {
				if (scale != 0.0D) {
					this.mapJObject.setZoom(scale);
				}
			} catch (Exception e) {
				assert Log.debugMessage("RunningThread - ERROR!!! - Failed setting scale.", Level.SEVERE);
			}
		}
	}

	/**
	 * @param layerIndex
	 *        ������ ����, �������� ��������������� ��������� ���������
	 * @param layerVisible
	 *        ��������� ����
	 * @param layerLabelsVisible
	 *        ��������� �������� ������� ����
	 */
	public void setLayerVisibility(final int layerIndex, final boolean layerVisible, final boolean layerLabelsVisible) {
		synchronized (this.mapJObject) {
			try {
				final FeatureLayer layer = (FeatureLayer) this.mapJObject.getLayers().get(layerIndex, LayerType.FEATURE);

				assert Log.debugMessage("RunningThread - Setting visibility for layer " + layer.getName(), Level.FINEST);

				layer.setEnabled(layerVisible);
				layer.setAutoLabel(layerLabelsVisible);
			} catch (Exception exc) {
				assert Log.debugMessage("RunningThread - ERROR!!! - Failed setting layer visibility.", Level.SEVERE);
			}
		}
	}

	public MapJ getMapJObject() {
		return this.mapJObject;
	}
}
