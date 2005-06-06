/**
 * $Id: MapApplicationModelFactory.java,v 1.1 2005/06/06 12:19:08 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * ��������� ����� �������� ������ ���������� ��� ������ � ������.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/06/06 12:19:08 $
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
