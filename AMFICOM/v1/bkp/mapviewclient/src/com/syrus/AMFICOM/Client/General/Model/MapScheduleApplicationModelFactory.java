/**
 * $Id: MapScheduleApplicationModelFactory.java,v 1.4 2005/01/30 15:34:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

/**
 * ��������� ����� �������� ������ ���������� ��� ������ � ������ 
 * � ������ "�����������".
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/01/30 15:34:56 $
 * @module mapviewclient_v1
 */
public class MapScheduleApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_BINDING, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		aModel.setUsable(MapApplicationModel.MODE_NODE_LINK, false);

		return aModel;
	}
}
