/**
 * $Id: MapImageLoader.java,v 1.3 2005/06/15 07:42:28 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/15 07:42:28 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader
{
	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 */
	ImageIcon renderMapImage(TopologicalRequest request)
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
