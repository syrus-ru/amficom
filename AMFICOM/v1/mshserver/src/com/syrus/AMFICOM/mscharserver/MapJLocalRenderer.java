/*-
 * $Id: MapJLocalRenderer.java,v 1.9 2005/10/30 14:48:59 bass Exp $
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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.graphics.LocalRenderer;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoublePoint;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.ImageRequestComposer;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.map.MapFeature;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/30 14:48:59 $
 * @module mscharserver
 */
final class MapJLocalRenderer {

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

	public MapJLocalRenderer() throws IllegalDataException {
		final String fileToLoad = ApplicationProperties.getString(MAPINFO_DEFINITION_FILE_LOCATION_KEY, DEFAULT_MDF_FILE_LOCATION);
		Log.debugMessage("MapJLocalRenderer<init> | RunningThread - Constructor - Initializing MapJ.", Log.DEBUGLEVEL07);
		this.mapJObject = createMapJ(fileToLoad);
	}

	private static synchronized MapJ createMapJ(final String fileToLoad) throws IllegalDataException {
		Log.debugMessage("RunningThread - Initializing MapJ instance...", Log.DEBUGLEVEL07);
		// instantiate a MapJ and set the bounds
		final MapJ returnValue = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try {
			Log.debugMessage("Loading geoset...", Log.DEBUGLEVEL07);
			returnValue.loadMapDefinition(fileToLoad);
			Log.debugMessage("Map definition " + fileToLoad + " has been loaded.",
					Log.DEBUGLEVEL07);
		} catch (IOException e) {
			final String msg = "MapJLocalRendererRunningThread.createMapJ |" + " - ERROR!!! - Can't load geoset: " + fileToLoad;
			Log.errorMessage(msg);
			throw new IllegalDataException(msg);
		}

		returnValue.setDistanceUnits(LinearUnit.meter);

		return returnValue;
	}

	synchronized byte[] render(final TopologicalImageQuery query) throws Exception {
		this.cancelEncoding = false;

		final int miWidth = query.getMapImageWidth();
		final int miHeight = query.getMapImageHeight();
		// Setting size, zoom and center point
		if ((this.imageBuffer == null) || (this.imageBuffer.getWidth() != miWidth) || (this.imageBuffer.getHeight() != miHeight)) {
			this.imageBuffer = new BufferedImage(miWidth, miHeight, BufferedImage.TYPE_USHORT_565_RGB);
			Log.debugMessage("RunningThread - Constructor - Creating MapXtreme renderer.", Log.DEBUGLEVEL07);
			this.renderer = new LocalRenderer(this.imageBuffer);
			Log.debugMessage("RunningThread - Constructor - MapXtreme renderer created.", Log.DEBUGLEVEL07);
		}

		this.setSize(miWidth, miHeight);
		this.setCenter(new DoublePoint(query.getTopoCenterX(), query.getTopoCenterY()));
		this.setScale(query.getTopoScale());

		final boolean[] layerVisibilities = query.getLayerVisibilities();
		final boolean[] labelVisibilities = query.getLabelVisibilities();

		for (int i = 0; i < query.getLayerVisibilities().length; i++) {
			this.setLayerVisibility(i, layerVisibilities[i], labelVisibilities[i]);
		}

		if (this.renderer == null) {
			Log.errorMessage("RunningThread - Constructor - Renderer is not initialized. "
					+ "Run setRenderingParameters first.");
			throw new IllegalDataException("MapJLocalRenderer.render | Failed to initialize Renderer");
		}

		Log.debugMessage("RenderToStream - Before rendering.", Log.DEBUGLEVEL07);

		try {
			this.renderer.render(ImageRequestComposer.create(this.mapJObject, NUM_OF_COLORS, BACKGROUND_COLOR, "image/png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		if (this.cancelEncoding) {
			return null;
		}

		// Output the map as a GIF Image
		final ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		Log.debugMessage("RenderToStream - Encoding image.", Log.DEBUGLEVEL07);
		ImageIO.write(this.imageBuffer, "PNG", resultStream);

		resultStream.flush();
		resultStream.close();

		final byte[] result = resultStream.toByteArray();
		Log.debugMessage("RenderToStream - Successfully rendered.", Log.DEBUGLEVEL07);
		return result;
	}

	void cancelRendering() throws Exception {
		Log.debugMessage("Stopping the rendering of map.", Log.DEBUGLEVEL07);

		this.cancelEncoding = true;
		this.renderer.interrupt();

		Log.debugMessage("Rendering stopped.", Log.DEBUGLEVEL07);
	}

	List<MapFeature> findFeature(final String searchName) throws IllegalDataException {
		final List<MapFeature> featureList = new LinkedList<MapFeature>();
		Log.debugMessage("Starting search procedure.", Level.INFO);
		for (final Iterator layersIt = this.mapJObject.getLayers().iterator(LayerType.FEATURE); layersIt.hasNext();) {
			FeatureLayer currLayer = (FeatureLayer) layersIt.next();
			Log.debugMessage("Searching at FeatureLayer: " + currLayer.getName(), Level.INFO);
			try {

				final List labelColumnsList = currLayer.getLabelProperties().getLabelColumns();
				if (labelColumnsList.isEmpty()) {
					// Log.debugMessage("No labels' column at the layer.", Log.INFO);
					continue;
				}

				// XXX: wtfit
				final String labelColumnName = (String) labelColumnsList.iterator().next();

				// Log.debugMessage("Got labels' column name: " + labelColumnName,
				// Log.INFO);

				final FeatureSet fs = currLayer.searchAll(Collections.singletonList(labelColumnName), null);
				Feature feature = null;
				// Loop until FeatureSet.getNextFeature() returns null
				while ((feature = fs.getNextFeature()) != null) {

					// XXX: wtfit 2
					final String featureName = feature.getAttribute(0).getString();

					if (featureName.toLowerCase().indexOf(searchName.toLowerCase()) > 0) {
						// Got feature name
						final DoublePoint featureCentre = feature.getGeometry().getBounds().center();
						featureList.add(new MapFeature(featureCentre.x, featureCentre.y, featureName));
					}
				}
			} catch (Exception e) {
				throw new IllegalDataException("MapJLocalRenderer.findFeature(String searchName) | " + e.getMessage());
			}
		}
		return featureList;
	}

// ---------------------------------------Функции, работающие с MapJ
// --------------------------
	/**
	 * Установить размер выходного изображения
	 * 
	 * @param width
	 *        Ширина
	 * @param height
	 *        Высота
	 */
	private void setSize(final int width, final int height) {
		Log.debugMessage("RunningThread - Setting size", Log.DEBUGLEVEL07);
		this.mapJObject.setDeviceBounds(new DoubleRect(0, 0, width, height));
	}

	/**
	 * Установить центральную точку вида карты
	 * 
	 * @param center
	 *        Топологические координаты центральной точки
	 */
	private void setCenter(final DoublePoint center) {
		Log.debugMessage("RunningThread - Setting center", Log.DEBUGLEVEL07);
		try {
			this.mapJObject.setCenter(new com.mapinfo.util.DoublePoint(center.x, center.y));
		} catch (Exception e) {
			Log.errorMessage("RunningThread - ERROR!!! "
					+ "- Failed setting center. Reason" + e.getMessage());
		}
	}

	/**
	 * Установить заданный масштаб вида карты
	 * 
	 * @param scale
	 *        Массштаб для карты
	 */
	private void setScale(final double scale) {
		Log.debugMessage("RunningThread - Setting scale", Log.DEBUGLEVEL07);
		try {
			if (scale != 0.0D) {
				this.mapJObject.setZoom(scale);
			}
		} catch (Exception e) {
			Log.errorMessage("RunningThread - ERROR!!! - Failed setting scale.");
		}
	}

	/**
	 * @param layerIndex Индекс слоя, которому устанавливаются параметры видимости
	 * @param layerVisible Видимость слоя
	 * @param layerLabelsVisible Видимость надписей данного слоя
	 */
	private void setLayerVisibility(final int layerIndex, final boolean layerVisible, final boolean layerLabelsVisible) {
		try {
			final FeatureLayer layer = (FeatureLayer) this.mapJObject.getLayers().get(layerIndex, LayerType.FEATURE);

			Log.debugMessage("RunningThread - Setting visibility for layer " + layer.getName(),
					Log.DEBUGLEVEL07);

			layer.setEnabled(layerVisible);
			layer.setAutoLabel(layerLabelsVisible);
		} catch (Exception exc) {
			Log.errorMessage("RunningThread - ERROR!!! - Failed setting layer visibility. Reason: "
					+ exc.getMessage());
		}
	}
}
