package com.syrus.AMFICOM.Client.General.Model;

public class DefaultOptimizeApplicationModelFactory
		extends OptimizeApplicationModelFactory
{
	public DefaultOptimizeApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("menuSessionSave", false);
		aModel.setUsable("menuSessionUndo", false);
//		aModel.setUsable("menuSchemeOpen", false);
//		aModel.setUsable("menuViewScheme", false);
		aModel.setSelected("menuOptimizeAuto", true);
		aModel.setUsable("menuOptimizeAuto", false);
		aModel.setUsable("menuOptimizeNext", false);

		aModel.setUsable("menuHelpContents", false);
		aModel.setUsable("menuHelpFind", false);
		aModel.setUsable("menuHelpTips", false);
		aModel.setUsable("menuHelpStart", false);
		aModel.setUsable("menuHelpCourse", false);
		aModel.setUsable("menuHelpHelp", false);
		aModel.setUsable("menuHelpSupport", false);
		aModel.setUsable("menuHelpLicnse", false);

    aModel.setUsable("mapActionViewProperties", true);// ��������� � ������������ �
    aModel.setUsable("mapActionEditProperties", false);// ������� ������ �� 24 �������� 2003

    aModel.setVisible("menuSessionOptions", false); // ��������� � ������������ � ������� ������ �� 13 ������ 2004

		return aModel;
	}
}