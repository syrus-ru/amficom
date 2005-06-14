/**
 * $Id: SimpleMapImageRenderer.java,v 1.4 2005/06/14 14:33:00 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/14 14:33:00 $
 * @author $Author: peskovsky $
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
}
