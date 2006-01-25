package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class DefaultSchematicsApplicationModelFactory
		extends SchematicsApplicationModelFactory
{
	public DefaultSchematicsApplicationModelFactory()
	{
		//Maybe nothing
	}

	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);
		
//		aModel.setVisible("menuPathSave", false);
//		aModel.setVisible("menuPathEdit", false);
		aModel.setVisible("menuPathAddStart", false);
		aModel.setVisible("menuPathAddEnd", false);
		aModel.setVisible("menuPathAddLink", false);
		aModel.setVisible("menuPathRemoveLink", false);
		aModel.setVisible("menuPathAutoCreate", false);
		aModel.setVisible("menuPathDelete", false);
//		aModel.setVisible("menuPathCancel", false);
		
		aModel.setVisible("menuSchemeLoad", false);
//		aModel.setVisible("menuSchemeSaveAs", false);
		
//		aModel.setVisible("menuSchemeExport", false);
						
		return aModel;
	}
}
