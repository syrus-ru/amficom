/**
 * $Id: MapImageLoader.java,v 1.4 2005/06/16 14:39:05 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/16 14:39:05 $
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
