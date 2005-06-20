/**
 * $Id: MapImageLoader.java,v 1.5 2005/06/20 15:18:10 peskovsky Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/20 15:18:10 $
 * @author $Author: peskovsky $
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
