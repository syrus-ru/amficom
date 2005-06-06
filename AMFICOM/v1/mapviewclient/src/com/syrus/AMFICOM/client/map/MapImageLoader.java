/**
 * $Id: MapImageLoader.java,v 1.1 2005/06/06 07:12:43 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.Client.Map;

import javax.swing.ImageIcon;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/06 07:12:43 $
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