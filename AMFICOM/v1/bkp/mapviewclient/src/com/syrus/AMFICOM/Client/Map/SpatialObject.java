/**
 * $Id: SpatialObject.java,v 1.2 2005/01/21 16:19:57 krupenn Exp $
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
 * ����������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see com.syrus.AMFICOM.Client.Map.Setup.SpatialSearchPanel
 */
public interface SpatialObject 
{
	/**
	 * ���������� ��� ��������������� �������
	 */
	String getLabel();
}
