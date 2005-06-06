/**
 * $Id: MapImageLoader.java,v 1.2 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import javax.swing.ImageIcon;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/06 12:20:29 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapImageLoader
{
	/**
	 * �������� ������ �� ������ �� ��������� ����������.
	 */
	void stopRenderingAtServer();
	
	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 */
	ImageIcon renderMapImageAtServer(TopologicalRequest request)
        throws MapConnectionException, MapDataException;
}