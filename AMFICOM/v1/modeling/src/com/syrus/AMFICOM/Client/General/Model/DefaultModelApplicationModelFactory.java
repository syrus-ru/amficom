package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.MapModelingApplicationModelFactory;

public class DefaultModelApplicationModelFactory extends ModelApplicationModelFactory {

	@Override
	public ApplicationModel create() {
		new ReflectometryAnalyseApplicationModelFactory().create();
		new SchematicsApplicationModelFactory().create();
		
		new MapModelingApplicationModelFactory().create();
		
		ApplicationModel aModel = super.create();

		aModel.setUsable("menuSessionSave", false);
		aModel.setUsable("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);
		
		aModel.setVisible("menuView", false);
		aModel.setVisible("menuViewSchemeOpen", false);
		aModel.setVisible(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS, false);

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