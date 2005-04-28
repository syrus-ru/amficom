/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.6 2005/04/28 12:55:23 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

/**
 * ������ ���������� �� ��������� - ���������, ����� ������� �� ���������
 * �������� ��� �� ������� ������������
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/04/28 12:55:23 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_CONTENTS, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_START, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_COURSE, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_HELP, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_SUPPORT, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_LICENSE, false);

		return aModel;
	}
}
