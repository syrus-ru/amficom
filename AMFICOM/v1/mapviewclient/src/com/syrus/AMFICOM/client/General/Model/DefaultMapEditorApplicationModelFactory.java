/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.1 2004/09/18 11:27:04 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/18 11:27:04 $
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

		aModel.setUsable("menuHelpContents", false);
		aModel.setUsable("menuHelpFind", false);
		aModel.setUsable("menuHelpTips", false);
		aModel.setUsable("menuHelpStart", false);
		aModel.setUsable("menuHelpCourse", false);
		aModel.setUsable("menuHelpHelp", false);
		aModel.setUsable("menuHelpSupport", false);
		aModel.setUsable("menuHelpLicense", false);

		return aModel;
	}
}
