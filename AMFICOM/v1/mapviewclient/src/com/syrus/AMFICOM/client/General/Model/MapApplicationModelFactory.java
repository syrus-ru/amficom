/**
 * $Id: MapApplicationModelFactory.java,v 1.3 2005/05/27 15:14:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * ��������� ����� �������� ������ ���������� ��� ������ � ������.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/05/27 15:14:54 $
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
