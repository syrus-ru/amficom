/**
 * $Id: SpatialLayer.java,v 1.9 2005/06/20 15:19:17 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Component;

/**
 * ���� �������������� �����. ���������� ����������� ������������ ������� ���.
 * ������� ������������� - ���������� ������������ �����
 * @author $Author: peskovsky $
 * @version $Revision: 1.9 $, $Date: 2005/06/20 15:19:17 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.client.map.operations.LayersPanel
 */
public interface SpatialLayer 
{
	/**
	 * ���������� ���� ��������� ���� �� ����� ��� ������� ���������� �������.
	 * @return ���� ���������
	 */
	boolean isVisible();
	
	/**
	 * ���������� ���� ��������� ����� ���� �� �����.
	 * @return ���� ��������� �����
	 */
	boolean isLabelVisible();

	/**
	 * ���������� ���� ��������� ���� �� ����� ��� �������� ��������.
	 * @return ���� ���������
	 */
	boolean isVisibleAtScale(double scale);
	
	/**
	 * ������������� ���� ��������� ���� �� ����� ��� ������� ���������� �������.
	 * @param visible ���� ���������
	 */
	void setVisible(boolean visible);
	
	/**
	 * ������������� ���� ��������� ����� ���� �� �����.
	 * @param visible ���� ��������� �����
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * ���������� ���������, ���������� ����������� ����������� �������� ����.
	 * ���� ����������� ���������� - ������ ���������� null
	 * @return ���������
	 */
	Component getLayerImage();

	/**
	 * ���������� �������� ����.
	 * @return �������� ����
	 */
	String getName();
}
