/**
 * $Id: SpatialObject.java,v 1.4 2005/03/02 12:37:45 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

/**
 * �������������� ������. ���������� ����������� ������������ ������� ���.
 * ������� ������������� ������ - � ������ ��������� �� ���������� �������
 * ����������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/03/02 12:37:45 $
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
