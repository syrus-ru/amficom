/**
 * $Id: MapImageRenderer.java,v 1.5 2005/06/16 10:57:19 krupenn Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/16 10:57:19 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageRenderer {
	void sizeChanged() throws MapConnectionException, MapDataException;
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
