/**
 * $Id: SpatialLayer.java,v 1.5 2005/04/15 11:12:32 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */
package com.syrus.AMFICOM.Client.Map;

import java.awt.Component;

/**
 * ���� �������������� �����. ���������� ����������� ������������ ������� ���.
 * ������� ������������� - ���������� ������������ �����
 * @author $Author: peskovsky $
 * @version $Revision: 1.5 $, $Date: 2005/04/15 11:12:32 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Operations.LayersPanel
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
	 * ���������� ���� ��������� ���� �� ����� ��� ������� ��������.
	 * @return ���� ���������
	 */
	boolean isVisibleAtCurrentScale();

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
