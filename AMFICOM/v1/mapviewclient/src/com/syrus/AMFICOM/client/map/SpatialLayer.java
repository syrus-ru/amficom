/*-
 * $$Id: SpatialLayer.java,v 1.11 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Component;

/**
 * ���� �������������� �����. ���������� ����������� ������������ ������� ���.
 * ������� ������������� - ���������� ������������ �����
 * 
 * @version $Revision: 1.11 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.operations.LayersPanel
 */
public interface SpatialLayer {
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
