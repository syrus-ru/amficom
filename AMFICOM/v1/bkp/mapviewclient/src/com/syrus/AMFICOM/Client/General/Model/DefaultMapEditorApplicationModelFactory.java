/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.2 2004/10/04 16:04:43 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/10/04 16:04:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

//		aModel.setVisible("menuMapOptions", false);
//		aModel.setVisible("menuMapCatalogue", false);

		aModel.setVisible("menuScheme", false);

		aModel.setVisible("menuHelpContents", false);
		aModel.setVisible("menuHelpFind", false);
		aModel.setVisible("menuHelpTips", false);
		aModel.setVisible("menuHelpStart", false);
		aModel.setVisible("menuHelpCourse", false);
		aModel.setVisible("menuHelpHelp", false);
		aModel.setVisible("menuHelpSupport", false);
		aModel.setVisible("menuHelpLicense", false);

		return aModel;
	}
}
