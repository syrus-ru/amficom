/**
 * $Id: SpatialLayer.java,v 1.10 2005/08/11 12:43:29 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.10 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
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
