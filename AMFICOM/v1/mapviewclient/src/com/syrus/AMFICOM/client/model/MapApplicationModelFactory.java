/**
 * $Id: MapApplicationModelFactory.java,v 1.2 2005/06/22 08:43:49 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;


/**
 * ��������� ����� �������� ������ ���������� ��� ������ � ������.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/06/22 08:43:49 $
 * @module mapviewclient_v1
 */
public class MapApplicationModelFactory
{
	/**
	 * ������� ������ ������ ����������.
	 * @return ������
	 */
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
