package com.syrus.AMFICOM.Client.General.Model;

public class DefaultConfigApplicationModelFactory
		extends ConfigApplicationModelFactory 
{
	public DefaultConfigApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", false);

		aModel.setVisible("menuSessionOptions", false);

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);

		aModel.setVisible("menuView", false);
		aModel.setVisible("menuViewRefresh", false);
		aModel.setVisible("menuViewCatalogue", false);
		aModel.setVisible("menuViewMessages", false);
		aModel.setVisible("menuViewToolbar", false);
		aModel.setVisible("menuViewMapScheme", false);

		aModel.setUsable("menuSchemeMap", false);
//		aModel.setUsable("menuSchemeNetScheme", false);
		aModel.setInstalled("menuSchemeNetAttribute", false);
		aModel.setInstalled("menuSchemeNetElType", false);
//		aModel.setUsable("menuSchemeNetElement", false);
		aModel.setVisible("menuSchemeJ", false);
		aModel.setUsable("menuSchemeJScheme", false);
		aModel.setUsable("menuSchemeJAttribute", false);
		aModel.setUsable("menuSchemeJElType", false);
		aModel.setUsable("menuSchemeJElement", false);

		aModel.setUsable("menuObjectNetDir", false);
		aModel.setUsable("menuObjectJDir", false);

		aModel.setVisible("menuNet", false);
		aModel.setUsable("menuNetDirLink", false);
		aModel.setUsable("menuNetDirEquipment", false);
		aModel.setInstalled("menuNetDirAddress", false);
		aModel.setInstalled("menuNetDirResource", false);
		aModel.setInstalled("menuNetDirProtocol", false);
		aModel.setInstalled("menuNetDirTechnology", false);
		aModel.setInstalled("menuNetDirInterface", false);
		aModel.setInstalled("menuNetDirPort", false);
		aModel.setInstalled("menuNetDirStack", false);

		aModel.setInstalled("menuNetCatResource", false);
		aModel.setInstalled("menuNetCatTPGroup", false);
		aModel.setInstalled("menuNetCatTestPoint", false);
		aModel.setInstalled("menuNetLocation", false);

		aModel.setVisible("menuJ", false);
		aModel.setUsable("menuJDirKIS", false);
		aModel.setInstalled("menuJDirAccessPoint", false);
		aModel.setInstalled("menuJDirLink", false);
		aModel.setInstalled("menuJCatAccessPoint", false);
		aModel.setInstalled("menuJCatResource", false);
		aModel.setInstalled("menuJInstall", false);

		aModel.setVisible("menuMaintain", false);
		aModel.setVisible("menuMaintainType", false);
		aModel.setVisible("menuMaintainEvent", false);
		aModel.setVisible("menuMaintainAlarmRule", false);
		aModel.setVisible("menuMaintainMessageRule", false);
		aModel.setVisible("menuMaintainAlertRule", false);
		aModel.setVisible("menuMaintainReactRule", false);
		aModel.setVisible("menuMaintainRule", false);
		aModel.setVisible("menuMaintainCorrectRule", false);

		aModel.setInstalled("menuTools", false);

		aModel.setInstalled("menuWindow", false);

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