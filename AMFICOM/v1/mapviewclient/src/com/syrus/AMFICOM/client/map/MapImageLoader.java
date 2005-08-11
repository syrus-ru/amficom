/**
 * $Id: MapImageLoader.java,v 1.7 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Image;
import java.util.List;

import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/11 12:43:29 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public interface MapImageLoader {
	public abstract MapConnection getMapConnection() throws MapConnectionException;

	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 */
	Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException;

	/**
	 * �������� ������ �� ������ �� ��������� ����������.
	 */
	void stopRendering() throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� �� ���������.
	 * @param searchText ����� ������
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	List findSpatialObjects(final String searchText) throws MapConnectionException, MapDataException;
	
}
