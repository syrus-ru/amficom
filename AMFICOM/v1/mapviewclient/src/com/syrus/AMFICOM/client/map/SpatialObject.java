/**
 * $Id: SpatialObject.java,v 1.3 2005/02/07 16:09:25 krupenn Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @see com.syrus.AMFICOM.Client.Map.Setup.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * ���������� ��� ��������������� �������.
	 * @return ��� �������
	 */
	String getLabel();
}
