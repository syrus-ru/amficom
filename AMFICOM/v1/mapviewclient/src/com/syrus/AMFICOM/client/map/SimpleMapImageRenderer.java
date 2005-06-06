/**
 * $Id: SimpleMapImageRenderer.java,v 1.2 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/06 12:20:29 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class SimpleMapImageRenderer implements MapImageRenderer {

	MapImageLoader loader;

	public SimpleMapImageRenderer(MapImageLoader loader) {
		this.loader = loader;
	}
	
	public void sizeChanged() throws MapConnectionException, MapDataException {
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
}
