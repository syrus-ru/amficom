/**
 * $Id: MapImageRenderer.java,v 1.8 2005/08/12 14:49:41 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/12 14:49:41 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public interface MapImageRenderer {
	void setSize(Dimension newSize) throws MapConnectionException, MapDataException;
	void setCenter(DoublePoint newCenter) throws MapConnectionException, MapDataException;
	void setScale(double newScale) throws MapConnectionException, MapDataException;
	void analyzeMouseLocation(MouseEvent event) throws MapDataException, MapConnectionException;
	void refreshLayers() throws MapConnectionException, MapDataException;
	Image getImage() throws MapConnectionException, MapDataException;
	Dimension getImageSize();
	void cancel();
	/**
	 * ������������ ��� ������� ������ ������ ��� �����.
	 * ����� ��������� � ��������� ����� ����� ��� ����������� ������ ����������� 
	 * @param point 
	 * @return ��������� �����
	 */
	DoublePoint getNearestCenter(DoublePoint point);

	MapImageLoader getLoader();
}
