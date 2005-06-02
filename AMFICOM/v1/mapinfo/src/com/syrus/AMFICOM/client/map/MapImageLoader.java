/**
 * $Id: MapImageLoader.java,v 1.1.2.2 2005/06/02 12:14:04 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.Client.Map;

import javax.swing.ImageIcon;

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