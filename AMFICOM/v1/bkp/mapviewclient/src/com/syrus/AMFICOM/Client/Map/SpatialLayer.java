/**
 * $Id: SpatialLayer.java,v 1.2 2005/02/03 16:24:59 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/02/03 16:24:59 $
 * @module mapviewclient_v1
 */
public interface SpatialLayer 
{
	/**
	 * ���������� ���� ��������� ���� �� �����.
	 */
	boolean isVisible();
	
	/**
	 * ���������� ���� ��������� ����� ���� �� �����.
	 */
	boolean isLabelVisible();
	
	/**
	 * ������������� ���� ��������� ���� �� �����.
	 */
	void setVisible(boolean visible);
	
	/**
	 * ������������� ���� ��������� ����� ���� �� �����.
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * ���������� ���������, ���������� ����������� ����������� �������� ����.
	 * ���� ����������� ���������� - ������ ���������� null
	 */
	Component getLayerImage();

	/**
	 * ���������� �������� ����.
	 * @return �������� ����
	 */
	String getName();
}
