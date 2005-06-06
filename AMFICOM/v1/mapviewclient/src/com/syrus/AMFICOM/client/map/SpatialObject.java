/**
 * $Id: SpatialObject.java,v 1.5 2005/06/06 12:20:29 krupenn Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/06 12:20:29 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Operations.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * ���������� ��� ��������������� �������.
	 * @return ��� �������
	 */
	String getLabel();
}
