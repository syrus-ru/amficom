/**
 * $Id: SpatialLayer.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public interface SpatialLayer 
{
	/**
	 * ���������� ���� ��������� ���� �� �����
	 */
	boolean isVisible();
	
	/**
	 * ���������� ���� ��������� ����� ���� �� �����
	 */
	boolean isLabelVisible();
	
	/**
	 * ������������� ���� ��������� ���� �� �����
	 */
	void setVisible(boolean visible);
	
	/**
	 * ������������� ���� ��������� ����� ���� �� �����
	 */
	void setLabelVisible(boolean visible);
	
	/**
	 * ���������� ���������, ���������� ����������� ����������� �������� ����.
	 * ���� ����������� ���������� - ������ ���������� null
	 */
	Component getLayerImage();

	/**
	 * ���������� �������� ����
	 */	
	String getName();
}
