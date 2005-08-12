/**
 * $Id: SimpleMapImageRenderer.java,v 1.7 2005/08/12 12:17:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.general.DoublePoint;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/12 12:17:59 $
 * @author $Author: krupenn $
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
	public void cancel()
	{
		// TODO Auto-generated method stub
		
	}

	public MapImageLoader getLoader() {
		return this.loader;
	}
}
