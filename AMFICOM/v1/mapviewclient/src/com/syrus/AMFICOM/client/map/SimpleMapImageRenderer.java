/*-
 * $$Id: SimpleMapImageRenderer.java,v 1.9 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class SimpleMapImageRenderer implements MapImageRenderer {

	private final MapContext mapContext;
	private final MapImageLoader loader;
	private final MapCoordinatesConverter coordsConverter;

	public SimpleMapImageRenderer(MapCoordinatesConverter coordsConverter, MapContext mapContext, MapImageLoader loader)
	{
		this.coordsConverter = coordsConverter;
		this.mapContext = mapContext;
		this.loader = loader;
	}
	
	public void setSize(Dimension newSize) throws MapConnectionException, MapDataException {
		// nothing
	}

	public void setCenter(DoublePoint newCenter) throws MapConnectionException, MapDataException {
		// nothing
	}

	public void setScale(double newScale) throws MapConnectionException, MapDataException {
		// nothing
	}

	public void analyzeMouseLocation(MouseEvent event) throws MapDataException, MapConnectionException {
		// nothing
	}

	public void refreshLayers() throws MapConnectionException, MapDataException {
		// nothing
	}

	public Image getImage() throws MapConnectionException, MapDataException {
		return null;
	}

	public Dimension getImageSize() {
		return null;
	}

	public DoublePoint getNearestCenter(DoublePoint point) {
		return point;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageRenderer#cancel()
	 */
	public void cancel() {
		// nothing to cancel
	}

	public MapImageLoader getLoader() {
		return this.loader;
	}
}
