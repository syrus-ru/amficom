/**
 * $Id: SpatialLayer.java,v 1.3 2005/02/07 16:09:25 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Setup.LayersPanel
 */
public interface SpatialLayer 
{
	/**
	 * ���������� ���� ��������� ���� �� �����.
	 * @return ���� ���������
	 */
	boolean isVisible();
	
	/**
	 * ���������� ���� ��������� ����� ���� �� �����.
	 * @return ���� ��������� �����
	 */
	boolean isLabelVisible();
	
	/**
	 * ������������� ���� ��������� ���� �� �����.
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
