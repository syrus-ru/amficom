/**
 * $Id: MapMapEditorApplicationModelFactory.java,v 1.6 2005/05/27 15:14:54 krupenn Exp $
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
 * ��������� ����� �������� ������ ���������� ��� ������ � ������ 
 * � ������ "�������� �������������� ����".
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/05/27 15:14:54 $
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
