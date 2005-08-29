/**
 * $Id: MapImageLoader.java,v 1.13 2005/08/29 11:29:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.13 $, $Date: 2005/08/29 11:29:40 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader {
	MapConnection getMapConnection() throws MapConnectionException;

	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 */
	Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException;

	/**
	 * �������� ������ �� ������ �� ��������� ����������.
	 */
	void stopRendering() throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� �� ��������� � ��������� ����.
	 * @param searchText ����� ������
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final String searchText) throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� � ��������� �������.
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final Rectangle2D.Double bounds) throws MapConnectionException, MapDataException;
}
