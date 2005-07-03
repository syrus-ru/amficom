/**
 * $Id: MapImageLoader.java,v 1.6 2005/06/22 08:43:46 krupenn Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/06/22 08:43:46 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader
{
	public abstract MapConnection getMapConnection()
		throws MapConnectionException;

	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 */
	Image renderMapImage(TopologicalImageQuery query)
        throws MapConnectionException, MapDataException;

	/**
	 * �������� ������ �� ������ �� ��������� ����������.
	 */
	void stopRendering()
		throws MapConnectionException, MapDataException;

	/**
	 * ���������� ����� �������������� �������� �� ���������.
	 * @param searchText ����� ������
	 * @return ������ ��������� �������� ({@link SpatialObject})
	 */
	List findSpatialObjects(String searchText)
		throws MapConnectionException, MapDataException;
	
}
