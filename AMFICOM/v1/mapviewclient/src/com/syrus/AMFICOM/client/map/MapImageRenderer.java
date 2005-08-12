/**
 * $Id: MapImageRenderer.java,v 1.7 2005/08/12 12:17:58 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.general.DoublePoint;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/12 12:17:58 $
 * @author $Author: krupenn $
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
