/*
 * $Id: MapJLocalRenderer.java,v 1.12 2006/06/22 11:46:37 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.12 $, $Date: 2006/06/22 11:46:37 $
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
	
	private boolean[] cachedLayerVisibilities;
	private boolean[] cachedLabelVisibilities;
	private DoublePoint center;
	private double scale;

	public MapJLocalRenderer(final String fileToLoad) throws IOException {
		Log.debugMessage("RunningThread - Constructor - Initializing MapJ.", Level.FINEST);
		this.initializeMapJ();
		this.setMapDefinition(fileToLoad);		
	}

	/**
	 * ������ ������ MapJ � ��������� ���������������� ������.
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
			Log.debugMessage("RunningThread - Constructor - Creating MapXtreme renderer.", Log.DEBUGLEVEL09);
			
			this.renderer = new LocalRenderer(this.imageBuffer);
			Log.debugMessage("RunningThread - Constructor - MapXtreme renderer created.", Log.DEBUGLEVEL09);
			
			this.setSize(miWidth, miHeight);
		}
		
		final double y = query.getTopoCenterY();
		final double x = query.getTopoCenterX();
		if (this.center == null || this.center.x != x || this.center.y != y) {
			this.center = new DoublePoint(x, y);
			setCenter(this.center);	
		}
		final double topoScale = query.getTopoScale();
		if (this.scale != topoScale) {
			this.scale = topoScale;
			this.setScale(this.scale);
		}

		final boolean[] layerVisibilities = query.getLayerVisibilities();
		final boolean[] labelVisibilities = query.getLabelVisibilities();
		
		if (this.cachedLabelVisibilities == null) {
			this.cachedLayerVisibilities = layerVisibilities;
			this.cachedLabelVisibilities = labelVisibilities;
		}
		
		for (int i = 0; i < layerVisibilities.length; i++) {
			if (this.cachedLabelVisibilities[i] != labelVisibilities[i] 
			   || this.cachedLayerVisibilities[i] != layerVisibilities[i]) {
				this.setLayerVisibility(i, layerVisibilities[i], labelVisibilities[i]);	
			}
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
				+ "\n		" + (time3 - time2) + " created new BufferedImage.", Log.DEBUGLEVEL09);

		return imageToReturn;
	}

	public void cancelRendering() throws Exception {
		this.stopRendering = true;
		Log.debugMessage("MapJRenderer - cancelRendering - Stopping the rendering of map.", Log.DEBUGLEVEL09);
		if (this.renderer != null) {
			this.renderer.interrupt();
		}
		Log.debugMessage("MapJRenderer - cancelRendering - Rendering stopped.", Log.DEBUGLEVEL09);
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
			Log.debugMessage("RunningThread - Setting size", Log.DEBUGLEVEL09);
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
			Log.debugMessage("RunningThread - Setting center", Log.DEBUGLEVEL09);
			try {
				this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
			} catch (Exception e) {
				Log.debugMessage("RunningThread - ERROR!!! - Failed setting center.", Level.SEVERE);
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
			Log.debugMessage("RunningThread - Setting scale", Log.DEBUGLEVEL09);
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

				Log.debugMessage("RunningThread - Setting visibility for layer " + layer.getName(), Log.DEBUGLEVEL09);

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
