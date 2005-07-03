/**
 * $Id: SpatialObject.java,v 1.6 2005/06/06 12:57:01 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:57:01 $
 * @module mapviewclient_v1
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
