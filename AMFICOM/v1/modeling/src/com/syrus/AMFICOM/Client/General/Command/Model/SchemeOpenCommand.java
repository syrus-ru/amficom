/*-
 * $Id: SchemeOpenCommand.java,v 1.1 2005/11/24 15:46:12 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;

public class SchemeOpenCommand extends AbstractCommand {
	ApplicationContext aContext;

	public SchemeOpenCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	@Override
	public void execute() {
		com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeOpenCommand command = 
				new com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeOpenCommand(this.aContext);
		command.execute();
		if (command.getResult() == Command.RESULT_OK) {
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS).execute();
			aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR).execute();
			aModel.getCommand(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS).execute();
			aModel.getCommand(ModelApplicationModel.MENU_WINDOW_TRANS_DATA).execute();
			aModel.getCommand(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS).execute();
			
			aModel.getCommand(ModelApplicationModel.MENU_WINDOW_SCHEME).execute();
			aModel.getCommand(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES).execute();
			aModel.getCommand(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES).execute();
			
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_PRIMARYPARAMETERS, true);
			aModel.setEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_TRANS_DATA, true);
			
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_SCHEME, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES, true);
			aModel.setEnabled(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES, true);
			
			aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
			
			aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			
			aModel.fireModelChanged("");
		}
	}
}
