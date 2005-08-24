/**
 * $Id: MapImageLoader.java,v 1.12 2005/08/24 08:19:58 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.12 $, $Date: 2005/08/24 08:19:58 $
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
	Set<SpatialObject> findSpatialObjects(final SpatialLayer layer, final String searchText) throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� � ��������� �������.
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	Set<SpatialObject> findSpatialObjects(final SpatialLayer layer, final Rectangle2D.Double bounds) throws MapConnectionException, MapDataException;
}
