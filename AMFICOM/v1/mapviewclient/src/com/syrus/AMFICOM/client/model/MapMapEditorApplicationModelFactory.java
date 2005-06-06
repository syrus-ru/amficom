/**
 * $Id: MapMapEditorApplicationModelFactory.java,v 1.1 2005/06/06 12:19:09 krupenn Exp $
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
 * ��������� ����� �������� ������ ���������� ��� ������ � ������ 
 * � ������ "�������� �������������� ����".
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/06/06 12:19:09 $
 * @module mapviewclient_v1
 */
public class MapMapEditorApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

//		aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
//		aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		return aModel;
	}
}
