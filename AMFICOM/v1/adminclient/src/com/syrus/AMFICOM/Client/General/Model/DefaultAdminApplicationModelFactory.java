package com.syrus.AMFICOM.Client.General.Model;

public class DefaultAdminApplicationModelFactory
    extends AdminApplicationModelFactory
{
  public DefaultAdminApplicationModelFactory()
  {
  }

  public ApplicationModel create()
  {
    ApplicationModel aModel = super.create();

//		aModel.setVisible("menuAccessMaintain", false);

		aModel.setVisible("menuSessionOptions", false);

    aModel.setUsable("menuSessionSave", false);
    aModel.setUsable("menuSessionUndo", false);
    aModel.setUsable("menuSessionDomain", false);

    aModel.setUsable("menuViewRefresh", false);
    aModel.setUsable("menuViewCatalogue", false);

    aModel.setUsable("menuArchConfigure", false);
    aModel.setUsable("menuArchSecurity", false);
    aModel.setUsable("menuArchRISD", false);
    aModel.setUsable("menuArchSW", false);
    aModel.setUsable("menuArchSupervise", false);

    aModel.setUsable("menuContainPool", false);
    aModel.setUsable("menuContainStore", false);

//		aModel.setUsable("menuUserCategory", false);
    aModel.setUsable("menuUserPrivilege", false);
    aModel.setUsable("menuOrganization", false);
    aModel.setUsable("menuSubscriber", false);
    aModel.setUsable("menuOperational", false);

    aModel.setInstalled("menuTools", false);

    aModel.setUsable("menuWindowList", false);

    aModel.setUsable("menuHelpContents", false);
    aModel.setUsable("menuHelpFind", false);
    aModel.setUsable("menuelpTips", false);
    aModel.setUsable("menuHelpStart", false);
    aModel.setUsable("menuHelpCourse", false);
    aModel.setUsable("menuHelpHelp", false);
    aModel.setUsable("menuHelpSupport", false);
    aModel.setUsable("menuHelpLiecnse", false);

    return aModel;
  }
}