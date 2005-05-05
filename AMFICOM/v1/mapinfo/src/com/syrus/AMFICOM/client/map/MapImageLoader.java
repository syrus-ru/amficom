/**
 * $Id: MapImageLoader.java,v 1.1.2.1 2005/05/05 10:22:15 krupenn Exp $
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
	void renderMapImageAtServer(TopologicalRequest request)  throws MapConnectionException;

	/**
	 * ���������� ����������� � ������� �� HTTP-�������
	 * @return �����������
	 */
	ImageIcon getServerMapImage();
}