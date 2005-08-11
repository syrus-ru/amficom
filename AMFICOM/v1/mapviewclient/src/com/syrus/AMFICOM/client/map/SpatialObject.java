/**
 * $Id: SpatialObject.java,v 1.7 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map;

/**
 * �������������� ������. ���������� ����������� ������������ ������� ���.
 * ������� ������������� ������ - � ������ ��������� �� ���������� �������
 * ����������� ��������.
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
 * @see com.syrus.AMFICOM.client.map.operations.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * ���������� ��� ��������������� �������.
	 * @return ��� �������
	 */
	String getLabel();
}
